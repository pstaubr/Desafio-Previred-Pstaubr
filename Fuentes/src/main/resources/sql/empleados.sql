-- public.empleados definition

-- Drop table

-- DROP TABLE public.empleados;

CREATE TABLE public.empleados (
	id serial4 NOT NULL,
	nombre varchar(50) NULL,
	apellido varchar(50) NULL,
	rut varchar(20) NOT NULL,
	cargo varchar(50) NULL,
	salario numeric(10, 2) NULL,
	bonos numeric(10, 2) NULL,
	descuentos numeric(10, 2) NULL,
	salario_base numeric(10, 2) NULL,
	salario_final numeric(10, 2) NULL,
	CONSTRAINT empleados_pkey PRIMARY KEY (id)
);