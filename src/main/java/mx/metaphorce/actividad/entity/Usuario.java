package mx.metaphorce.actividad.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String contrasenia;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date fechaRegistro;

    @ManyToMany
    @JoinTable(
            name = "amistades",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "amigo_id")
    )
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<Usuario> amigos;

    @ManyToMany(mappedBy = "amigos")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<Usuario> amigosDe;

    @OneToMany(mappedBy = "remitente")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private Set<Mensaje> mensajesEnviados;

    @OneToMany(mappedBy = "destinatario")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private Set<Mensaje> mensajesRecibidos;
}
