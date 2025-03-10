    document.addEventListener('DOMContentLoaded', function() {
        cargarEmpleados();

        document.getElementById('enviarBtn').addEventListener('click', function() {
            limpiarErrores();

            const nombre = document.getElementById('nombre').value;
            const apellido = document.getElementById('apellido').value;
            const rut = document.getElementById('rut').value;
            const cargo = document.getElementById('cargo').value;
            const salario = parseMiles(document.getElementById('salario').value);
            const bonos = parseMiles(document.getElementById('bonos').value);
            const descuentos = parseMiles(document.getElementById('descuentos').value);

            let errores = false;

            if (!nombre) {
                mostrarError('nombre', 'Nombre es requerido.');
                errores = true;
            }

            if (!apellido) {
                mostrarError('apellido', 'Apellido es requerido.');
                errores = true;
            }

            if (!rut) {
                mostrarError('rut', 'RUT es requerido.');
                errores = true;
            } else if (!validarRut(rut)) {
                mostrarError('rut', 'RUT inv&aacutelido.');
                errores = true;
            }

            if (!cargo) {
                mostrarError('cargo', 'Cargo es requerido.');
                errores = true;
            }

            if (!salario || salario < 400000) {
                mostrarError('salario', 'Salario debe ser mayor o igual a $400,000.');
                errores = true;
            }

            if (bonos > salario * 0.5) {
                mostrarError('bonos', 'El bono no puede ser mayor al 50% del salario base.');
                errores = true;
            }

            if (descuentos > salario) {
                mostrarError('descuentos', 'El total de descuentos no puede ser mayor al salario base.');
                errores = true;
            }

            if (errores) {
                return;
            }

            enviarDatos(nombre, apellido, rut, cargo, salario, bonos, descuentos);
        });
    });

    function limpiarErrores() {
        const errores = document.querySelectorAll('.error');
        errores.forEach(error => error.textContent = '');
    }

    function mostrarError(campo, mensaje) {
        document.getElementById(campo + 'Error').textContent = mensaje;
    }

    function validarRut(rut) {
        rut = rut.replace(/\./g, '').replace('-', '');
        if (rut.length < 2) {
            return false;
        }
        return true;
    }

    function cargarEmpleados() {
        const xhr = new XMLHttpRequest();
        xhr.open('GET', '/previred-desafio-pstaubr/api/empleados');
        xhr.onload = function() {
            if (xhr.status === 200) {
                const empleados = JSON.parse(xhr.responseText);
                mostrarEmpleados(empleados);
            } else {
                alert('Error al cargar empleados.');
            }
        };
        xhr.onerror = function() {
            alert('Error de conexi&oacute;n.');
        };
        xhr.send();
    }

    function mostrarEmpleados(empleados) {
        const tableBody = document.getElementById('empleadosTable').getElementsByTagName('tbody')[0];
        tableBody.innerHTML = '';

        if (empleados.length === 0) {
            document.getElementById('mensajeSinRegistros').style.display = 'block';
        } else {
            document.getElementById('mensajeSinRegistros').style.display = 'none';
            empleados.forEach(empleado => {
                const row = tableBody.insertRow();
                row.insertCell(0).textContent = empleado.id;
                row.insertCell(1).textContent = empleado.nombre;
                row.insertCell(2).textContent = empleado.apellido;
                row.insertCell(3).textContent = empleado.rut;
                row.insertCell(4).textContent = empleado.cargo;
                row.insertCell(5).textContent = empleado.bonos.toLocaleString('es-CL');
                row.insertCell(6).textContent = empleado.descuentos.toLocaleString('es-CL');
                row.insertCell(7).textContent = empleado.salarioBase.toLocaleString('es-CL');
                row.insertCell(8).textContent = empleado.salarioFinal.toLocaleString('es-CL');

                // Agregar botón "Eliminar"
                const deleteButton = document.createElement('button');
                deleteButton.textContent = 'Eliminar';
                deleteButton.addEventListener('click', function() {
                    if (confirm('¿Est&aacute;s seguro de que deseas eliminar a este empleado?')) {
                        eliminarEmpleado(empleado.id);
                    }
                });
                row.insertCell(9).appendChild(deleteButton); // Columna "Acciones"
            });
        }
    }

    function eliminarEmpleado(id) {
        const xhr = new XMLHttpRequest();
        xhr.open('DELETE', `/previred-desafio-pstaubr/api/empleados?id=${id}`);
        xhr.onload = function() {
            if (xhr.status === 200) {
                alert('Empleado eliminado exitosamente.');
                cargarEmpleados(); // Recargar la tabla
            } else {
                alert('Error al eliminar empleado.');
            }
        };
        xhr.onerror = function() {
            alert('Error de conexi&oacute;n.');
        };
        xhr.send();
    }

    function enviarDatos(nombre, apellido, rut, cargo, salario, bonos, descuentos) {
        const xhr = new XMLHttpRequest();
        xhr.open('POST', '/previred-desafio-pstaubr/api/empleados');
        xhr.setRequestHeader('Content-Type', 'application/json');

        xhr.onload = function() {
            if (xhr.status === 201) {
                alert('Empleado creado exitosamente.');
                cargarEmpleados();
                document.getElementById('empleadoForm').reset();
            } else {
                try {
                    const respuesta = JSON.parse(xhr.responseText);
                    alert('Error: ' + respuesta.errores.join(', '));
                } catch (e) {
                    alert('Error inesperado al crear el empleado. Por favor, revisa la consola para más detalles.');
                    console.error('Error al parsear la respuesta del servidor:', e);
                    console.log('Respuesta del servidor:', '"' + xhr.responseText + '"');
                }
            }
        };

        xhr.onerror = function() {
            alert('Error de conexi&oacute;n al servidor. Por favor, intenta nuevamente más tarde.');
            console.error('Error de conexi&oacute;n:', xhr.status);
        };

        xhr.send(JSON.stringify({
            nombre: nombre,
            apellido: apellido,
            rut: rut,
            cargo: cargo,
            salario: salario,
            bonos: bonos,
            descuentos: descuentos
        }));
    }

    function formatoMiles(input) {
        let value = input.value.replace(/\./g, '');
        if (!value) return;
        value = parseInt(value);
        input.value = value.toLocaleString('es-CL');
    }

    function parseMiles(value) {
        if (!value) return 0;
        return parseInt(value.replace(/\./g, ''));
    }
function mostrarEmpleados(empleados) {
    const tableBody = document.getElementById('empleadosTable').getElementsByTagName('tbody')[0];
    tableBody.innerHTML = '';

    if (empleados.length === 0) {
        document.getElementById('mensajeSinRegistros').style.display = 'block';
    } else {
        document.getElementById('mensajeSinRegistros').style.display = 'none';
        empleados.forEach(empleado => {
            const row = tableBody.insertRow();
            row.insertCell(0).textContent = empleado.id;
            row.insertCell(1).textContent = empleado.nombre;
            row.insertCell(2).textContent = empleado.apellido;
            row.insertCell(3).textContent = empleado.rut;
            row.insertCell(4).textContent = empleado.cargo;
            row.insertCell(5).textContent = empleado.salario.toLocaleString('es-CL');
            row.insertCell(6).textContent = empleado.bonos.toLocaleString('es-CL');
            row.insertCell(7).textContent = empleado.descuentos.toLocaleString('es-CL');

            // Agregar botón "Eliminar"
            const deleteButton = document.createElement('button');
            deleteButton.textContent = 'Eliminar';
            deleteButton.addEventListener('click', function() {
                if (confirm('¿Est&aacute;s seguro de que deseas eliminar a este empleado?')) {
                    eliminarEmpleado(empleado.id);
                }
            });
            row.insertCell(8).appendChild(deleteButton); // Columna "Acciones"
        });
    }
}