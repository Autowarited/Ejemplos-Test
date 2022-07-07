package util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @author jrodriguez
 * @version 1.0
 */
@Component
public class NumberGenerator {

    /**
     * @param idCategory  identificador de que tipo de orden se trata. Compra/Venta/Facturacion
     *                    Compra -> 1
     *                    Venta -> 2
     *                    Facturacion -> 3
     * @param idExist ultimo numero de orden persistido en la base de datos
     * @return number; identificador de la nueva orden.
     * Formato de valor a devolver = "V-2022-6-1"
     */
    public String generateNumberOrder(Integer idCategory, Long idExist) {

        String prefix = getPrefix(idCategory);
        String year = String.valueOf(LocalDate.now().getYear());
        String month = String.valueOf(LocalDate.now().getMonth().getValue());
        String suffix = getSuffix(idExist);

        String separator = "-";
        return prefix.concat(separator)
                .concat(year).concat(separator)
                .concat(month).concat(separator)
                .concat(suffix);
    }


    private String getPrefix(Integer idCategory) {
        String prefix;
        switch (idCategory) {
            case 1 -> prefix = "C";
            case 2 -> prefix = "V";
            case 3 -> prefix = "F";
            default -> throw new IllegalStateException("Unexpected value: " + idCategory);
        }
        return prefix;
    }

    private String getSuffix(Long numberExist) {
        String suffix;

        if (numberExist == null)
            suffix = String.valueOf(1);
        else
            suffix = String.valueOf((numberExist + 1));

        return suffix;
    }
}
