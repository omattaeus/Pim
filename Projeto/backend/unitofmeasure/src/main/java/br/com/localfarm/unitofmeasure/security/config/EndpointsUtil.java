package br.com.localfarm.unitofmeasure.security.config;

public class EndpointsUtil {

    // Endpoints acessíveis para todos
    public static final String[] ENDPOINTS_ANYONE_CAN_ACCESS = {
            "/api/users/register",
            "/api/users/login"
    };

    // Endpoints acessíveis apenas para Administrador Geral
    public static final String[] ENDPOINTS_WITH_ADMINISTRADOR_CAN_ACCESS = {
           "/units-of-measure",
            "/units-of-measure/**/**"
    };

    // Endpoints acessíveis para Administrador Geral e Gerencial Fazenda
    public static final String[] ENDPOINTS_WITH_GERENCIAL_CAN_ACCESS = {
            "/units-of-measure",
            "/units-of-measure/**"
    };

    // Endpoints acessíveis para Administrador Geral, Gerencial Fazenda e Operacional Fazenda
    public static final String[] ENDPOINTS_WITH_OPERACIONAL_CAN_ACCESS = {
            "/units-of-measure",
            "/units-of-measure/**"
    };
}