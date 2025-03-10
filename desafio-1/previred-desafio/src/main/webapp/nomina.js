document.addEventListener('DOMContentLoaded', function() {
    const uploadForm = document.getElementById('uploadForm');
    const uploadButton = document.getElementById('uploadButton');
    const resultsTable = document.getElementById('resultsTable');
    const mensajeSinRegistros = document.getElementById('mensajeSinRegistros');
    const paginacionDiv = document.getElementById('paginacion');
    const loadingMessage = document.getElementById('loadingMessage'); // Obtener el mensaje de carga

    uploadButton.addEventListener('click', function() {
        const fileInput = document.getElementById('csvFile');
        const file = fileInput.files[0];

        if (!file) {
            alert('Por favor, selecciona un archivo CSV.');
            return;
        }

        const formData = new FormData();
        formData.append('csvFile', file);

        loadingMessage.style.display = 'block'; // Mostrar el mensaje de carga

        fetch('/previred-desafio-pstaubr/api/nominas/calcular', {
            method: 'POST',
            body: formData
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al subir el archivo.');
            }
            return response.json();
        })
        .then(data => {
            loadingMessage.style.display = 'none'; // Ocultar el mensaje de carga
            mostrarResultados(data);
        })
        .catch(error => {
            loadingMessage.style.display = 'none'; // Ocultar el mensaje de carga en caso de error
            console.error('Error:', error);
            alert('Ocurrió un error al procesar el archivo.');
        });
    });

    function mostrarResultados(empleados, paginaActual = 1, registrosPorPagina = 1000) {
        const resultsTableBody = resultsTable.getElementsByTagName('tbody')[0]; // Obtener el tbody
        resultsTableBody.innerHTML = ''; // Limpiar la tabla
        paginacionDiv.innerHTML = ''; // Limpiar la paginación

        if (empleados.length === 0) {
            mensajeSinRegistros.style.display = 'block';
            resultsTable.style.display = 'none';
            return;
        } else {
            mensajeSinRegistros.style.display = 'none';
            resultsTable.style.display = 'table';
        }

        const totalRegistros = empleados.length;
        const totalPaginas = Math.ceil(totalRegistros / registrosPorPagina);

        const indiceInicio = (paginaActual - 1) * registrosPorPagina;
        const indiceFin = Math.min(indiceInicio + registrosPorPagina, totalRegistros);

        const empleadosPagina = empleados.slice(indiceInicio, indiceFin);

        empleadosPagina.forEach(empleado => {
            const row = resultsTableBody.insertRow(); // Usar resultsTableBody

            row.insertCell(0).textContent = empleado.rut;
            row.insertCell(1).textContent = empleado.nombre;
            row.insertCell(2).textContent = empleado.apellido;
            row.insertCell(3).textContent = empleado.cargo;
            row.insertCell(4).textContent = empleado.salarioBase;
            row.insertCell(5).textContent = empleado.bonos;
            row.insertCell(6).textContent = empleado.descuentos;
            row.insertCell(7).textContent = empleado.salarioFinal;
        });

        if (totalPaginas > 1) {
            for (let i = 1; i <= totalPaginas; i++) {
                const botonPagina = document.createElement('button');
                botonPagina.textContent = i;
                botonPagina.addEventListener('click', () => {
                    mostrarResultados(empleados, i, registrosPorPagina);
                });
                paginacionDiv.appendChild(botonPagina);
            }
        }
    }
});