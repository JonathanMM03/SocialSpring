package mx.metaphorce.actividad.service;

import mx.metaphorce.actividad.entity.Usuario;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.nio.ExportException;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Map;
import java.util.Set;

@Service
public class GrafoService {

    @Autowired
    private UsuarioService usuarioService;

    public String generarGrafoDeAmigos(Long userId) {
        Set<Usuario> amigos = usuarioService.obtenerAmigosRecursivamente(userId);
        Graph<Usuario, DefaultEdge> grafo = new SimpleGraph<>(DefaultEdge.class);

        for (Usuario usuario : amigos) {
            grafo.addVertex(usuario);
            for (Usuario amigo : usuario.getAmigos()) {
                grafo.addVertex(amigo);
                grafo.addEdge(usuario, amigo);
            }
        }

        return exportarAGraphviz(grafo);
    }

    private String exportarAGraphviz(Graph<Usuario, DefaultEdge> grafo) {
        DOTExporter<Usuario, DefaultEdge> exporter = new DOTExporter<>(v -> String.valueOf(v.getId()));
        exporter.setVertexAttributeProvider(this::crearAtributosDeVertice);
        
        StringWriter writer = new StringWriter();
        try {
            exporter.exportGraph(grafo, writer);
        } catch (ExportException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    private Map<String, Attribute> crearAtributosDeVertice(Usuario usuario) {
        return Map.of(
            "label", DefaultAttribute.createAttribute(usuario.getNombre())
        );
    }
}
