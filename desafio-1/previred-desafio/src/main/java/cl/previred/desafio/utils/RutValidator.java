package cl.previred.desafio.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase utilitaria para validar el RUT chileno.
 */
public class RutValidator {

    private static final Logger logger = LoggerFactory.getLogger(RutValidator.class);

    /**
     * Valida si un RUT dado es válido.
     *
     * @param rut El RUT a validar (con o sin puntos y guión).
     * @return true si el RUT es válido, false en caso contrario.
     */
    public static boolean validarRut(String rut) {
        if (rut == null || rut.isEmpty()) {
            logger.warn("Validación de RUT fallida: RUT nulo o vacío.");
            return false;
        }

        // Remover puntos y guión
        rut = rut.replace(".", "").replace("-", "");
        logger.debug("RUT normalizado: {}", rut);

        // Validar largo del RUT
        if (rut.length() < 2) {
            logger.warn("Validación de RUT fallida: Largo inválido. RUT: {}", rut);
            return false;
        }

        // Separar número y dígito verificador
        String numero = rut.substring(0, rut.length() - 1);
        char dv = rut.charAt(rut.length() - 1);
        logger.debug("Número RUT: {}, Dígito verificador: {}", numero, dv);

        // Validar que el número sea numérico
        try {
            Integer.parseInt(numero);
        } catch (NumberFormatException e) {
            logger.warn("Validación de RUT fallida: Número RUT no numérico. RUT: {}", rut);
            return false;
        }

        // Calcular dígito verificador esperado
        char dvCalculado = calcularDigitoVerificador(numero);
        logger.debug("Dígito verificador calculado: {}", dvCalculado);

        // Comparar dígito verificador calculado con el entregado
        boolean valido = dv == dvCalculado;
        if (valido) {
            logger.debug("RUT válido: {}", rut);
        } else {
            logger.warn("Validación de RUT fallida: Dígito verificador inválido. RUT: {}", rut);
        }
        return valido;
    }

    /**
     * Calcula el dígito verificador para un número de RUT dado.
     *
     * @param numero El número de RUT sin dígito verificador.
     * @return El dígito verificador calculado.
     */
    private static char calcularDigitoVerificador(String numero) {
        int suma = 0;
        int multiplicador = 2;

        for (int i = numero.length() - 1; i >= 0; i--) {
            suma += Integer.parseInt(String.valueOf(numero.charAt(i))) * multiplicador;
            multiplicador++;
            if (multiplicador == 8) {
                multiplicador = 2;
            }
        }

        int resto = suma % 11;
        int dvCalculado = 11 - resto;

        if (dvCalculado == 10) {
            return 'K';
        } else if (dvCalculado == 11) {
            return '0';
        } else {
            return (char) (dvCalculado + '0');
        }
    }
}