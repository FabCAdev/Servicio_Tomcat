package org.lasalle.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("glucosa")
public class RestGlucosa {
    
    @Path("saludar")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response saludar(){
        String respuesta = """
                           {"response" : "Hola desde Java"}
                           """;
        return Response.ok(respuesta).build();
    }
    
    @Path("saludarPersonalizado")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response saludarPersonalizado(
        @QueryParam("nombre") String nombre
    ){
        String respuesta = """
                           {"response" : "Hola %s desde Java"}
                           """;
        respuesta = String.format(respuesta, nombre);
        return Response.ok(respuesta).build();
    }

    @Path("cuadrado")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response calcularCuadrado(
        @QueryParam("numero") int numero
    ){
        int resultado = numero * numero;
        String respuesta = """
                           {"numero" : %d, "resultado" : %d}
                           """;
        respuesta = String.format(respuesta, numero, resultado);
        return Response.ok(respuesta).build();
    }
}