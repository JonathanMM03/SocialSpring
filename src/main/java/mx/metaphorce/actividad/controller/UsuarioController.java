package mx.metaphorce.actividad.controller;

import mx.metaphorce.actividad.entity.Usuario;
import mx.metaphorce.actividad.service.GrafoService;
import mx.metaphorce.actividad.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private GrafoService grafoService;

    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrarUsuario(@Validated @RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @Validated @RequestBody Usuario usuarioDetalles) {
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioDetalles);
        return usuarioActualizado != null ?
                new ResponseEntity<>(usuarioActualizado, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/buscar/email/{email}")
    public ResponseEntity<Usuario> buscarPorEmail(@PathVariable String email) {
        Usuario usuario = usuarioService.buscarPorEmail(email);
        return usuario != null ?
                new ResponseEntity<>(usuario, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<Usuario>> buscarPorNombreContaining(@PathVariable String nombre) {
        List<Usuario> usuarios = usuarioService.buscarPorNombreContaining(nombre);
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarTodos() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/amigos/{id}")
    public ResponseEntity<List<Usuario>> obtenerAmigosPorID(@PathVariable Long id) {
        List<Usuario> amigos = usuarioService.obtenerAmigosPorID(id);
        return amigos != null ?
                new ResponseEntity<>(amigos, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/amigos/email/{email}")
    public ResponseEntity<List<Usuario>> obtenerAmigosPorEmail(@PathVariable String email) {
        List<Usuario> amigos = usuarioService.obtenerAmigosPorEmail(email);
        return amigos != null ?
                new ResponseEntity<>(amigos, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/agregarAmigos/{idUsuario1}/{idUsuario2}")
    public ResponseEntity<Usuario> agregarAmigos(@PathVariable Long idUsuario1, @PathVariable Long idUsuario2) {
        Usuario usuario = usuarioService.agregarAmigos(idUsuario1, idUsuario2);
        return usuario != null ?
                new ResponseEntity<>(usuario, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}/amigos")
    public ResponseEntity<List<Usuario>> obtenerAmigos(@PathVariable Long id) {
        List<Usuario> amigos = usuarioService.obtenerAmigos(id);
        if (amigos != null) {
            return ResponseEntity.ok(amigos);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Grafos
    //https://dreampuf.github.io/GraphvizOnline/
    @GetMapping("/{id}/grafo")
    public ResponseEntity<String> obtenerGrafoDeAmigos(@PathVariable Long id) {
        String dotFormat = grafoService.generarGrafoDeAmigos(id);
        return ResponseEntity.ok(dotFormat);
    }
}

