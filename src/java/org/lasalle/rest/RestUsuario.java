package org.lasalle.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.lasalle.controller.ControllerUsuario;
import org.lasalle.model.Usuario;

@Path("usuarios")
public class RestUsuario {

    private final Gson gson = new Gson();
    private final ControllerUsuario controller = new ControllerUsuario();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsuarios() {
        try {
            List<Usuario> lista = controller.getAll();
            return Response.ok(gson.toJson(lista)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return error(Response.Status.INTERNAL_SERVER_ERROR,
                    "No se pudo obtener la lista de usuarios");
        }
    }

    @GET
    @Path("getAllUsuarios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsuariosAlias() {
        return getAllUsuarios();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveUsuario(
            @FormParam("nombre") String nombre,
            @FormParam("primerApellido") String primerApellido,
            @FormParam("segundoApellido") String segundoApellido,
            @FormParam("email") String email) {

        String errorValidacion = validar(nombre, primerApellido, email);
        if (errorValidacion != null) {
            return error(Response.Status.BAD_REQUEST, errorValidacion);
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre.trim());
        usuario.setPrimerApellido(primerApellido.trim());
        usuario.setSegundoApellido(
                segundoApellido == null || segundoApellido.isBlank() ? null : segundoApellido.trim());
        usuario.setEmail(email.trim());

        try {
            Usuario creado = controller.save(usuario);

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("result", "Usuario registrado");
            body.put("usuario", creado);

            return Response.status(Response.Status.CREATED)
                    .entity(gson.toJson(body))
                    .build();
        } catch (SQLIntegrityConstraintViolationException e) {
            return error(Response.Status.CONFLICT, "El email ya está registrado");
        } catch (Exception e) {
            e.printStackTrace();
            return error(Response.Status.INTERNAL_SERVER_ERROR,
                    "No se pudo registrar el usuario");
        }
    }

    private String validar(String nombre, String primerApellido, String email) {
        if (nombre == null || nombre.isBlank()) {
            return "El nombre es obligatorio";
        }
        if (primerApellido == null || primerApellido.isBlank()) {
            return "El primer apellido es obligatorio";
        }
        if (email == null || email.isBlank()) {
            return "El email es obligatorio";
        }
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            return "El email no tiene un formato válido";
        }
        return null;
    }

    private Response error(Response.Status status, String mensaje) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("result", mensaje);
        return Response.status(status).entity(gson.toJson(body)).build();
    }
}
