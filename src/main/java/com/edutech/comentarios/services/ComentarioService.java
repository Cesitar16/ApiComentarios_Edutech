package com.edutech.comentarios.services;

import com.edutech.comentarios.dto.ComentarioDTO;
import com.edutech.comentarios.dto.CursoInfoDTO;
import com.edutech.comentarios.dto.UsuarioInfoDTO;
import com.edutech.comentarios.models.Comentario;
import com.edutech.comentarios.models.Curso;
import com.edutech.comentarios.models.Usuario;
import com.edutech.comentarios.repository.ComentarioRepository;
import com.edutech.comentarios.repository.CursoRepository;
import com.edutech.comentarios.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    // Guarda un nuevo comentario
    public ComentarioDTO guardar(ComentarioDTO dto) {
        Comentario comentario = toEntity(dto);
        Comentario saved = repository.save(comentario);
        return toDTO(saved);
    }

    // Devuelve una lista de todos los comentarios
    public List<ComentarioDTO> listar() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Busca un comentario por su ID
    public Optional<ComentarioDTO> obtenerPorId(Integer id) {
        return repository.findById(id)
                .map(this::toDTO);
    }

    // Actualiza un comentario existente
    public Optional<ComentarioDTO> actualizar(Integer id, ComentarioDTO dto) {
        return repository.findById(id)
                .map(comentario -> {
                    comentario.setCalificacion(dto.getCalificacion());
                    comentario.setComentario(dto.getComentario());
                    comentario.setFecha(dto.getFecha());
                    
                    // Actualiza las relaciones
                    toEntityRelationships(comentario, dto);
                    
                    Comentario actualizado = repository.save(comentario);
                    return toDTO(actualizado);
                });
    }

    // Elimina un comentario
    public boolean eliminar(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Métodos personales que hice en el repository
    
    public List<ComentarioDTO> buscarPorCurso(Integer cursoId) {
        return repository.findByCursoIdCurso(cursoId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ComentarioDTO> buscarPorUsuario(Integer usuarioId) {
        return repository.findByUsuarioIdUser(usuarioId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    //Son métodos de conversión que traducen el objeto Entidad (que es el mapa de la tabla en la base de datos) a un DTO, que es un objeto limpio y legible para la API.
    //Y al revés, convierten el DTO (los datos que vienen de la petición) en una Entidad para que el sistema pueda guardarla en la base de datos, enlazando las relaciones
    //con otras tablas en el proceso

    private void toEntityRelationships(Comentario comentario, ComentarioDTO dto) {
        if (dto.getUsuario() != null && dto.getUsuario().getIdUser() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuario().getIdUser())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + dto.getUsuario().getIdUser()));
            comentario.setUsuario(usuario);
        }
        if (dto.getCurso() != null && dto.getCurso().getIdCurso() != null) {
            Curso curso = cursoRepository.findById(dto.getCurso().getIdCurso())
                    .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado con ID: " + dto.getCurso().getIdCurso()));
            comentario.setCurso(curso);
        }
    }

    private Comentario toEntity(ComentarioDTO dto) {
        Comentario comentario = new Comentario();
        comentario.setIdComentario(dto.getIdComentario());
        comentario.setCalificacion(dto.getCalificacion());
        comentario.setComentario(dto.getComentario());
        comentario.setFecha(dto.getFecha() != null ? dto.getFecha() : LocalDate.now());
        
        toEntityRelationships(comentario, dto);
        
        return comentario;
    }

    private ComentarioDTO toDTO(Comentario comentario) {
        ComentarioDTO dto = new ComentarioDTO();
        dto.setIdComentario(comentario.getIdComentario());
        dto.setCalificacion(comentario.getCalificacion());
        dto.setComentario(comentario.getComentario());
        dto.setFecha(comentario.getFecha());

        if (comentario.getUsuario() != null) {
            dto.setUsuario(toUsuarioInfoDTO(comentario.getUsuario()));
        }
        if (comentario.getCurso() != null) {
            dto.setCurso(toCursoInfoDTO(comentario.getCurso()));
        }

        return dto;
    }

    private UsuarioInfoDTO toUsuarioInfoDTO(Usuario usuario) {
        UsuarioInfoDTO dto = new UsuarioInfoDTO();
        dto.setIdUser(usuario.getIdUser());
        dto.setUsername(usuario.getUsername());
        return dto;
    }

    private CursoInfoDTO toCursoInfoDTO(Curso curso) {
        CursoInfoDTO dto = new CursoInfoDTO();
        dto.setIdCurso(curso.getIdCurso());
        dto.setNombreCurso(curso.getNombreCurso());
        return dto;
    }
}
