package br.com.senac.kjdistribuidorasoftware.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Validacao {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String TELEFONE_PATTERN = "\\(\\d{2}\\) \\d{4,5}-\\d{4}";
    private static final String VALOR_MONETARIO_PATTERN = "^\\d{1,3}(\\.\\d{3})*(,\\d{2})?$";
    private static final String PESO_PATTERN = "^\\d{1,3}(\\.\\d{1,3})?$";

    private static Pattern padraoEmail = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
    private static Pattern padraoTelefone = Pattern.compile(TELEFONE_PATTERN, Pattern.CASE_INSENSITIVE);
    private static Pattern padraoMonetario = Pattern.compile(VALOR_MONETARIO_PATTERN, Pattern.CASE_INSENSITIVE);
    private static Pattern padraoPeso = Pattern.compile(PESO_PATTERN, Pattern.CASE_INSENSITIVE);

    public static boolean validarEmail(String email) {
        Matcher matcher = padraoEmail.matcher(email);
        return matcher.matches();
    }

    public static boolean validarTelefone(String telefone) {
        Matcher matcher = padraoTelefone.matcher(telefone);
        return matcher.matches();
    }

    public static boolean validarValor(String valor) {
        Matcher matcher = padraoMonetario.matcher(valor);
        return matcher.matches();
    }

    public static boolean validarPeso(String peso) {
        Matcher matcher = padraoPeso.matcher(peso);
        return matcher.matches();
    }

    public static String removerMascara(String texto) {
        return texto.replaceAll("[^0-9]", "");
    }

    public static String removerCifrao(String texto) {
        return texto.replaceAll("[R\\$\\s]", "").trim();
    }
}
