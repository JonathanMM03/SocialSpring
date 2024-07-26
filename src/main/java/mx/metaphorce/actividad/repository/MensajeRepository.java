package mx.metaphorce.actividad.repository;

import mx.metaphorce.actividad.entity.Mensaje;
import mx.metaphorce.actividad.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    // Método para encontrar todos los mensajes enviados por un usuario específico
    List<Mensaje> findByRemitente(Usuario remitente);

    // Método para encontrar todos los mensajes recibidos por un usuario específico
    List<Mensaje> findByDestinatario(Usuario destinatario);
}
