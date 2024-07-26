package mx.metaphorce.actividad.service;

import mx.metaphorce.actividad.entity.Usuario;
import mx.metaphorce.actividad.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario registrarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(Long id, Usuario usuarioDetalles) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setNombre(usuarioDetalles.getNombre());
            usuario.setContrasenia(usuarioDetalles.getContrasenia());
            usuario.setEmail(usuarioDetalles.getEmail());
            return usuarioRepository.save(usuario);
        } else {
            return null; // o lanza una excepción si prefieres manejar el caso de no encontrar el usuario
        }
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> buscarPorNombreContaining(String nombre) {
        return usuarioRepository.findByNombreContaining(nombre);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> obtenerAmigosPorID(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        return usuarioOptional.map(Usuario::getAmigosDe).orElse(null);
    }

    public List<Usuario> obtenerAmigosPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        return usuario != null ? usuario.getAmigosDe() : null;
    }

    public Usuario agregarAmigos(Long idUsuario1, Long idUsuario2) {
        Optional<Usuario> usuario1Optional = usuarioRepository.findById(idUsuario1);
        Optional<Usuario> usuario2Optional = usuarioRepository.findById(idUsuario2);

        if (usuario1Optional.isPresent() && usuario2Optional.isPresent()) {
            Usuario usuario1 = usuario1Optional.get();
            Usuario usuario2 = usuario2Optional.get();

            if (!usuario1.getAmigos().contains(usuario2)) {
                usuario1.getAmigos().add(usuario2);
            }

            if (!usuario2.getAmigos().contains(usuario1)) {
                usuario2.getAmigos().add(usuario1);
            }

            usuarioRepository.save(usuario1);
            usuarioRepository.save(usuario2);

            return usuario1;
        } else {
            throw new IllegalArgumentException("Uno o ambos usuarios no existen en la base de datos.");
        }
    }

    public Set<Usuario> obtenerAmigosRecursivamente(Long userId) {
        Set<Usuario> visited = new HashSet<>();
        Usuario usuario = usuarioRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        obtenerAmigosRecursivamente(usuario, visited);
        return visited;
    }

    private void obtenerAmigosRecursivamente(Usuario usuario, Set<Usuario> visited) {
        if (!visited.contains(usuario)) {
            visited.add(usuario);
            for (Usuario amigo : usuario.getAmigos()) {
                obtenerAmigosRecursivamente(amigo, visited);
            }
        }
    }

    public List<Usuario> obtenerAmigos(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            return usuario.getAmigos();
        }
        return null;
    }
}
