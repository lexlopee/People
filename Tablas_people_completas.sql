DROP SCHEMA IF EXISTS people CASCADE;
CREATE SCHEMA people AUTHORIZATION postgres;

SET search_path TO people;

-- 1. TABLA RANGOS
CREATE TABLE people.rango (
    id_rango INTEGER generated always as identity,
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT,
    nivel NUMERIC(3),
    minimo_aportado NUMERIC(15,2),
    minimo_donaciones NUMERIC(15,2),

    constraint pk_id_rang primary key(id_rango),
    constraint ck_niv_rang check (nivel between 0 and 100)
);

-- 2. TABLA USUARIOS
CREATE TABLE people.usuario (
    id_usuario INTEGER generated always as identity,
    id_rango INTEGER,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    rol VARCHAR(50),
    fecha_alt DATE NOT NULL DEFAULT CURRENT_DATE,
    fecha_baja DATE, -- Si es NULL está activo. Si tiene fecha, es la fecha de su baja.

    constraint pk_id_usu primary Key(id_usuario),
    constraint ck_rol_usu check (rol in('administrador','creador','donante')),
    constraint fk_id_rang_usu foreign key (id_rango) references people.rango(id_rango)
);

-- 3. TABLA CATEGORIA
CREATE TABLE people.categoria (
    id_categoria INTEGER GENERATED ALWAYS AS IDENTITY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    
    CONSTRAINT pk_id_cat PRIMARY KEY(id_categoria)
);

-- 4. TABLA ORGANIZACION (Actualizada con Pilar Legal y Financiero)
CREATE TABLE people.organizacion_promotora (
    id_organizacion INTEGER GENERATED ALWAYS AS IDENTITY,
    id_usuario INTEGER NOT NULL,
    
    -- Identidad Legal
    tipo_entidad VARCHAR(20) NOT NULL, -- PROMOTORA o PERSONA_JURIDICA
    razon_social VARCHAR(300) NOT NULL,
    cif_nif VARCHAR(20) NOT NULL UNIQUE,
    representante_legal VARCHAR(150) NOT NULL, -- Mujer responsable
    dni_representante VARCHAR(20) NOT NULL,
    
    -- Información Financiera (Stripe/Banco)
    iban VARCHAR(34) NOT NULL,
    swift_bic VARCHAR(11) NOT NULL,
    email_stripe VARCHAR(100),
    
    -- Localización y Facturación
    direccion_fiscal TEXT NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    codigo_postal VARCHAR(10) NOT NULL,
    pais VARCHAR(50) DEFAULT 'España',
    
    -- Estado
    estado_verificacion VARCHAR(50) DEFAULT 'pendiente',
    
    CONSTRAINT pk_id_org PRIMARY KEY(id_organizacion),
    CONSTRAINT fk_id_usu_org FOREIGN KEY (id_usuario) REFERENCES people.usuario(id_usuario),
    CONSTRAINT ck_tipo_ent_org CHECK (tipo_entidad IN ('PROMOTORA', 'PERSONA_JURIDICA')),
    CONSTRAINT ck_est_org CHECK (estado_verificacion IN ('pendiente', 'verificada', 'rechazada'))
);

-- 5. TABLA DOCUMENTOS_VALIDACION (Nueva tabla para KYC/Blanqueo de capitales)
CREATE TABLE people.documentos_validacion (
    id_documento INTEGER GENERATED ALWAYS AS IDENTITY,
    id_organizacion INTEGER NOT NULL,
    tipo_documento VARCHAR(50) NOT NULL,
    url_archivo VARCHAR(255) NOT NULL,
    estado_validacion VARCHAR(20) DEFAULT 'pendiente',
    fecha_subida TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_id_doc PRIMARY KEY(id_documento),
    CONSTRAINT fk_id_org_doc FOREIGN KEY (id_organizacion) REFERENCES people.organizacion_promotora(id_organizacion),
    CONSTRAINT ck_tipo_doc CHECK (tipo_documento IN (
        'DNI_FRONTAL', 'DNI_TRASERO', 'MODELO_036_037', 'CERTIFICADO_CENSAL', 
        'RECIBO_AUTONOMO', 'ESCRITURA_CONSTITUCION', 'CIF_DEFINITIVO', 
        'ESTATUTOS', 'ACTA_CONSTITUCION', 'COMPOSICION_JUNTA', 'TITULARIDAD_REAL',
        'RESOLUCION_REGISTRO', 'CERTIFICADO_BANCARIO'
    )),
    CONSTRAINT ck_est_doc CHECK (estado_validacion IN ('pendiente', 'aceptado', 'rechazado'))
);

-- 6. TABLA CAMPAÑA
CREATE TABLE people.campaña (
    id_campaña INTEGER GENERATED ALWAYS AS IDENTITY,
    id_usuario INTEGER NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    descripcion_larga TEXT,
    monto_objetivo NUMERIC(15,2) NOT NULL,
    monto_actual NUMERIC(15,2) DEFAULT 0,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    estado VARCHAR(50),
    
    CONSTRAINT pk_id_cam PRIMARY KEY(id_campaña),
    CONSTRAINT fk_id_usu_cam FOREIGN KEY (id_usuario) REFERENCES people.usuario(id_usuario),
    CONSTRAINT ck_mon_cam CHECK (monto_objetivo > 0),
    CONSTRAINT ck_fec_cam CHECK (fecha_fin > fecha_inicio)
);

-- 7. TABLA INTERMEDIA CAMPAÑA_CATEGORIA
CREATE TABLE people.campaña_categoria (
    id_campaña INTEGER NOT NULL,
    id_categoria INTEGER NOT NULL,
    
    CONSTRAINT pk_cam_cat PRIMARY KEY(id_campaña, id_categoria),
    CONSTRAINT fk_id_cam_rel FOREIGN KEY (id_campaña) REFERENCES people.campaña(id_campaña),
    CONSTRAINT fk_id_cat_rel FOREIGN KEY (id_categoria) REFERENCES people.categoria(id_categoria)
);

-- 8. TABLA DONACION
CREATE TABLE people.donacion (
    id_donacion INTEGER GENERATED ALWAYS AS IDENTITY,
    id_donante INTEGER NOT NULL,
    id_campaña INTEGER NOT NULL,
    monto NUMERIC(15,2) NOT NULL,
    fecha_pago TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado_pago VARCHAR(50),
    comision_plataforma NUMERIC(15,2),
    divisa VARCHAR(10) DEFAULT 'EUR',
    id_transaccion_pasarela VARCHAR(100),
    
    CONSTRAINT pk_id_don PRIMARY KEY(id_donacion),
    CONSTRAINT fk_id_donante FOREIGN KEY (id_donante) REFERENCES people.usuario(id_usuario),
    CONSTRAINT fk_id_cam_don FOREIGN KEY (id_campaña) REFERENCES people.campaña(id_campaña),
    CONSTRAINT ck_mon_don CHECK (monto > 0)
);

-- 9. TABLA COBRO 
CREATE TABLE people.cobro (
    id_cobro INTEGER GENERATED ALWAYS AS IDENTITY,
    id_campaña INTEGER NOT NULL,
    monto_neto NUMERIC(15,2) NOT NULL,
    fecha_solicitud TIMESTAMP NOT NULL,
    fecha_transferencia TIMESTAMP,
    cuenta_destino VARCHAR(100),
    comprobante_pago TEXT,
    
    CONSTRAINT pk_id_cob PRIMARY KEY(id_cobro),
    CONSTRAINT fk_id_cam_cob FOREIGN KEY (id_campaña) REFERENCES people.campaña(id_campaña)
);

-- 10. TABLA TRANSACCION
CREATE TABLE people.transaccion (
    id_transaccion INTEGER GENERATED ALWAYS AS IDENTITY,
    id_donacion INTEGER NOT NULL,
    tipo VARCHAR(50),
    
    CONSTRAINT pk_id_tra PRIMARY KEY(id_transaccion),
    CONSTRAINT fk_id_don_tra FOREIGN KEY (id_donacion) REFERENCES people.donacion(id_donacion)
);

-- 11. TABLA COMENTARIOS
CREATE TABLE people.comentarios (
    id_comentario INTEGER GENERATED ALWAYS AS IDENTITY,
    id_usuario INTEGER NOT NULL,
    id_campaña INTEGER NOT NULL,
    contenido TEXT NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT pk_id_com PRIMARY KEY(id_comentario),
    CONSTRAINT fk_id_usu_com FOREIGN KEY (id_usuario) REFERENCES people.usuario(id_usuario),
    CONSTRAINT fk_id_cam_com FOREIGN KEY (id_campaña) REFERENCES people.campaña(id_campaña)
);

-- 12. TABLA CAMPAIGN UPDATES
CREATE TABLE people.campaign_updates (
    id_update INTEGER GENERATED ALWAYS AS IDENTITY,
    id_campaña INTEGER NOT NULL,
    id_usuario INTEGER NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    descripcion TEXT,
    fecha_publicacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_solicitud TIMESTAMP,
    version VARCHAR(20),
    
    CONSTRAINT pk_id_upd PRIMARY KEY(id_update),
    CONSTRAINT fk_id_cam_upd FOREIGN KEY (id_campaña) REFERENCES people.campaña(id_campaña),
    CONSTRAINT fk_id_usu_upd FOREIGN KEY (id_usuario) REFERENCES people.usuario(id_usuario)
);

-- 13. TABLA REPORTES
CREATE TABLE people.reportes (
    id_reporte INTEGER GENERATED ALWAYS AS IDENTITY,
    id_usuario INTEGER NOT NULL,
    id_campaña INTEGER NOT NULL,
    motivo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    estado VARCHAR(50) DEFAULT 'pendiente',
    fecha_reporte TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT pk_id_rep PRIMARY KEY(id_reporte),
    CONSTRAINT fk_id_usu_rep FOREIGN KEY (id_usuario) REFERENCES people.usuario(id_usuario),
    CONSTRAINT fk_id_cam_rep FOREIGN KEY (id_campaña) REFERENCES people.campaña(id_campaña)
);