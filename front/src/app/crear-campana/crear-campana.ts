import { Component, OnInit, Inject, PLATFORM_ID, inject, ChangeDetectorRef, NgZone } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { RouterLink, Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

interface CategoriaItem { idCategoria: number; nombre: string; }

@Component({
  selector: 'app-crear-campana',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, FormsModule],
  templateUrl: './crear-campana.html',
  styleUrls: ['./crear-campana.css']
})
export class CrearCampana implements OnInit {

  private sanitizer = inject(DomSanitizer);

  form: FormGroup;
  categorias: CategoriaItem[] = [];
  categoriasSeleccionadas: number[] = [];  // múltiples categorías
  paso = 1;
  // Mini-formulario de solicitud
  solicitudMotivo = '';
  solicitudOrganizacion = '';
  solicitudEnviada = false;
  guardando = false;
  errorMsg = '';

  imagenSeleccionada: File | null = null;
  imagenPreview: string | null = null;

  get fechaMinima(): string {
    const d = new Date(); d.setDate(d.getDate() + 1);
    return d.toISOString().split('T')[0];
  }

  get diasPreview(): number {
    const fin = this.form.value.fechaFin;
    if (!fin) return 0;
    return Math.max(0, Math.ceil((new Date(fin).getTime() - Date.now()) / 86400000));
  }

  get nombresCategoriasSeleccionadas(): string {
    return this.categorias
      .filter(c => this.categoriasSeleccionadas.includes(c.idCategoria))
      .map(c => c.nombre)
      .join(', ') || 'Ninguna seleccionada';
  }

  get categoriasSeleccionadasObj(): CategoriaItem[] {
    return this.categorias.filter(c => this.categoriasSeleccionadas.includes(c.idCategoria));
  }

  get descripcionHtml(): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(this.markdownToHtml(this.form.value.descripcionLarga ?? ''));
  }

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    public router: Router,
    private route: ActivatedRoute,
    public authService: AuthService,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.form = this.fb.group({
      titulo:           ['', [Validators.required, Validators.minLength(10)]],
      descripcionLarga: ['', [Validators.required, Validators.minLength(50)]],
      montoObjetivo:    [null, [Validators.required, Validators.min(100)]],
      fechaFin:         ['', Validators.required],
      ubicacion:        ['España']
    });
  }

  ngOnInit(): void {
    if (!this.authService.estaLogueado()) { this.router.navigate(['/login']); return; }
    if (isPlatformBrowser(this.platformId)) {
      this.ngZone.run(() => {
        this.http.get<CategoriaItem[]>('http://localhost:8080/api/categorias').subscribe({
          next: (data) => {
            this.categorias = data;
            // Preseleccionar la categoría desde la que se llegó
            const categoriaId = this.route.snapshot.queryParamMap.get('categoriaId');
            if (categoriaId) {
              const id = parseInt(categoriaId, 10);
              if (!isNaN(id) && !this.categoriasSeleccionadas.includes(id)) {
                this.categoriasSeleccionadas = [id];
              }
            }
            this.cdr.detectChanges();
          },
          error: () => { this.errorMsg = 'No se pudieron cargar las categorías'; this.cdr.detectChanges(); }
        });
      });
    }
  }

  toggleCategoria(id: number): void {
    const idx = this.categoriasSeleccionadas.indexOf(id);
    if (idx === -1) {
      this.categoriasSeleccionadas = [...this.categoriasSeleccionadas, id];
    } else {
      this.categoriasSeleccionadas = this.categoriasSeleccionadas.filter(c => c !== id);
    }
    this.cdr.detectChanges();
  }

  esCategoriaSeleccionada(id: number): boolean {
    return this.categoriasSeleccionadas.includes(id);
  }

  insertarFormato(tipo: string): void {
    const textarea = document.querySelector('.crear-textarea') as HTMLTextAreaElement;
    if (!textarea) return;
    const start = textarea.selectionStart;
    const end = textarea.selectionEnd;
    const texto = textarea.value;
    const seleccion = texto.substring(start, end);
    const antes = texto.substring(0, start);
    const despues = texto.substring(end);
    let nuevo = '';
    let cursorOffset = 0;
    switch (tipo) {
      case 'negrita':       nuevo = `**${seleccion || 'texto en negrita'}**`; cursorOffset = seleccion ? nuevo.length : 2; break;
      case 'cursiva':       nuevo = `*${seleccion || 'texto en cursiva'}*`; cursorOffset = seleccion ? nuevo.length : 1; break;
      case 'lista':         nuevo = seleccion ? seleccion.split('\n').map(l => `- ${l}`).join('\n') : `\n- Elemento 1\n- Elemento 2\n- Elemento 3`; cursorOffset = nuevo.length; break;
      case 'lista-num':     nuevo = seleccion ? seleccion.split('\n').map((l,i) => `${i+1}. ${l}`).join('\n') : `\n1. Primer punto\n2. Segundo punto\n3. Tercer punto`; cursorOffset = nuevo.length; break;
      case 'titulo':        nuevo = `\n## ${seleccion || 'Título de sección'}`; cursorOffset = nuevo.length; break;
      case 'separador':     nuevo = `\n\n---\n\n`; cursorOffset = nuevo.length; break;
      case 'emoji-corazon': nuevo = '💜'; cursorOffset = 2; break;
      case 'emoji-check':   nuevo = '✅'; cursorOffset = 2; break;
      case 'emoji-info':    nuevo = 'ℹ️'; cursorOffset = 2; break;
      case 'emoji-warning': nuevo = '⚠️'; cursorOffset = 2; break;
      case 'emoji-estrella':nuevo = '⭐'; cursorOffset = 2; break;
      default: return;
    }
    this.form.patchValue({ descripcionLarga: antes + nuevo + despues });
    setTimeout(() => { textarea.focus(); const pos = start + cursorOffset; textarea.setSelectionRange(pos, pos); }, 10);
  }

  private markdownToHtml(md: string): string {
    if (!md) return '<p class="text-gray-400 italic">La descripción aparecerá aquí...</p>';
    let html = md
      .replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
      .replace(/^### (.+)$/gm, '<h3 class="md-h3">$1</h3>')
      .replace(/^## (.+)$/gm, '<h2 class="md-h2">$1</h2>')
      .replace(/^# (.+)$/gm, '<h1 class="md-h1">$1</h1>')
      .replace(/\*\*\*(.+?)\*\*\*/g, '<strong><em>$1</em></strong>')
      .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
      .replace(/\*(.+?)\*/g, '<em>$1</em>')
      .replace(/^---$/gm, '<hr class="md-hr">')
      .replace(/^- (.+)$/gm, '<li class="md-li">$1</li>')
      .replace(/^\d+\. (.+)$/gm, '<li class="md-li md-li-num">$1</li>')
      .replace(/\n\n/g, '</p><p class="md-p">')
      .replace(/\n/g, '<br>');
    html = html
      .replace(/(<li class="md-li">.*?<\/li>(\s*<br>)*)+/g, m => `<ul class="md-ul">${m}</ul>`)
      .replace(/(<li class="md-li md-li-num">.*?<\/li>(\s*<br>)*)+/g, m => `<ol class="md-ol">${m}</ol>`);
    return `<p class="md-p">${html}</p>`;
  }

  onImagenSeleccionada(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files?.[0]) return;
    const file = input.files[0];
    if (!file.type.startsWith('image/')) { this.errorMsg = 'Solo se permiten imágenes'; return; }
    if (file.size > 5 * 1024 * 1024) { this.errorMsg = 'Máximo 5MB'; return; }
    this.imagenSeleccionada = file;
    this.errorMsg = '';
    const reader = new FileReader();
    reader.onload = (e) => this.imagenPreview = e.target?.result as string;
    reader.readAsDataURL(file);
  }

  onDragOver(event: DragEvent): void { event.preventDefault(); }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    const file = event.dataTransfer?.files[0];
    if (!file) return;
    this.onImagenSeleccionada({ target: { files: [file] } } as any);
  }

  quitarImagen(): void { this.imagenSeleccionada = null; this.imagenPreview = null; }

  previsualizacion(): void {
    if (this.categoriasSeleccionadas.length === 0) {
      this.errorMsg = 'Selecciona al menos una categoría';
      return;
    }
    if (this.form.valid) this.paso = 2;
    else { this.form.markAllAsTouched(); this.errorMsg = 'Rellena todos los campos obligatorios'; }
  }

  volver(): void { this.paso = 1; this.errorMsg = ''; }

  // Antes "publicar" - ahora abre el mini-formulario de solicitud
  publicar(): void {
    if (!this.form.valid || this.categoriasSeleccionadas.length === 0) return;
    this.paso = 3; // ir al mini-formulario
    this.errorMsg = '';
    this.cdr.detectChanges();
  }

  // Envia la SOLICITUD (no crea la campaña, la crea el admin al aprobar)
  enviarSolicitud(): void {
    if (!this.solicitudMotivo.trim() || !this.solicitudOrganizacion.trim()) {
      this.errorMsg = 'Rellena el motivo y la organización';
      return;
    }
    this.guardando = true;
    this.errorMsg = '';

    const headers = { Authorization: `Bearer ${this.authService.getToken()}` };
    const payload = {
      titulo:        this.form.value.titulo,
      descripcion:   this.form.value.descripcionLarga,
      montoObjetivo: this.form.value.montoObjetivo,
      fechaFin:      this.form.value.fechaFin,
      categoriasIds: this.categoriasSeleccionadas.join(','),
      motivo:        this.solicitudMotivo.trim(),
      organizacion:  this.solicitudOrganizacion.trim()
    };

    this.http.post<any>('http://localhost:8080/api/solicitudes', payload, { headers }).subscribe({
      next: () => {
        this.guardando = false;
        this.solicitudEnviada = true;
        this.paso = 4; // pantalla de confirmacion
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.guardando = false;
        this.errorMsg = err.error ?? 'Error al enviar la solicitud';
        this.cdr.detectChanges();
      }
    });
  }

  private subirImagen(id: number, headers: any): void {
    const formData = new FormData();
    formData.append('imagen', this.imagenSeleccionada!);
    this.http.post(`http://localhost:8080/api/campanias/${id}/imagen`, formData, { headers, responseType: 'text' })
      .subscribe({ next: () => { this.guardando = false; this.router.navigate(['/campana', id]); },
                   error: () => { this.guardando = false; this.router.navigate(['/campana', id]); } });
  }

  campoInvalido(campo: string): boolean {
    const c = this.form.get(campo);
    return !!(c?.invalid && c?.touched);
  }
}