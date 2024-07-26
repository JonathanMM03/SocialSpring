package mx.metaphorce.actividad.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "mensajes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String contenido;

    @ManyToOne
    @JoinColumn(name = "remitente_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private Usuario remitente;

    @ManyToOne
    @JoinColumn(name = "destinatario_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private Usuario destinatario;
}
