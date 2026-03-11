--DROP SCHEMA IF EXISTS people CASCADE;
CREATE SCHEMA people AUTHORIZATION postgres;
--Tabla rangos
CREATE TABLE people.rango (
    id_rango INTEGER generated always as identity,
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT,
    nivel NUMERIC(3) ,
    minimo_aportado NUMERIC(15,2),
    minimo_donaciones NUMERIC(15,2),
    
    constraint pk_id_rang primary key(id_rango),
    constraint ck_niv_rang check (nivel between 0 and 100)
    
);
 CREATE TABLE people.usuario (
    id_usuario INTEGER generated always as identity ,
    id_rango INTEGER,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    rol VARCHAR(50),
    fecha_alt DATE NOT NULL,
 	fecha_baja DATE NOT null,
 	
 	constraint pk_id_usu primary Key( id_usuario),
 	constraint ck_rol_usu check (rol in('administrador','creador','donante')),
 	constraint fk_id_rang_usu foreign key (id_rango)references people.rango(id_rango)
);
 