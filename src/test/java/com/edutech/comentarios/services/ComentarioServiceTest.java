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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

// Se importa assertThat y assertThatThrownBy de AssertJ
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComentarioServiceTest {

    @Mock
    private ComentarioRepository comentarioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private ComentarioService comentarioService;

    private Usuario usuario;
    private Curso curso;
    private Comentario comentario;
    private ComentarioDTO comentarioDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUser(1);
        usuario.setUsername("testuser");

        curso = new Curso();
        curso.setIdCurso(101);
        curso.setNombreCurso("Curso de Mockito");

        UsuarioInfoDTO usuarioInfo = new UsuarioInfoDTO();
        usuarioInfo.setIdUser(1);
        usuarioInfo.setUsername("testuser");

        CursoInfoDTO cursoInfo = new CursoInfoDTO();
        cursoInfo.setIdCurso(101);
        cursoInfo.setNombreCurso("Curso de Mockito");

        comentarioDTO = new ComentarioDTO();
        comentarioDTO.setIdComentario(1);
        comentarioDTO.setComentario("¡Gran curso!");
        comentarioDTO.setCalificacion(5);
        comentarioDTO.setFecha(LocalDate.now());
        comentarioDTO.setUsuario(usuarioInfo);
        comentarioDTO.setCurso(cursoInfo);

        comentario = new Comentario();
        comentario.setIdComentario(1);
        comentario.setComentario("¡Gran curso!");
        comentario.setCalificacion(5);
        comentario.setFecha(LocalDate.now());
        comentario.setUsuario(usuario);
        comentario.setCurso(curso);
    }

    @Test
    @DisplayName("Debería guardar un comentario exitosamente")
    void testGuardar() {
        // Arrange
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(cursoRepository.findById(101)).thenReturn(Optional.of(curso));
        when(comentarioRepository.save(any(Comentario.class))).thenReturn(comentario);

        // Act
        ComentarioDTO resultado = comentarioService.guardar(comentarioDTO);

        // Assert: Usando AssertJ
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdComentario()).isEqualTo(1);
        assertThat(resultado.getUsuario().getUsername()).isEqualTo("testuser");

        verify(comentarioRepository, times(1)).save(any(Comentario.class));
    }

    @Test
    @DisplayName("Debería lanzar EntityNotFoundException si el usuario no existe al guardar")
    void testGuardarComentarioUsuarioNoEncontrado() {
        // Arrange
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert: Usando assertThatThrownBy de AssertJ
        assertThatThrownBy(() -> comentarioService.guardar(comentarioDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Usuario no encontrado");

        verify(comentarioRepository, never()).save(any());
    }


    @Test
    @DisplayName("Debería listar todos los comentarios")
    void testListarComentarios() {
        // Arrange
        when(comentarioRepository.findAll()).thenReturn(List.of(comentario));

        // Act
        List<ComentarioDTO> resultados = comentarioService.listar();

        // Assert
        assertThat(resultados).isNotEmpty().hasSize(1);
        assertThat(resultados.get(0).getComentario()).isEqualTo("¡Gran curso!");
    }

    @Test
    @DisplayName("Debería devolver una lista vacía si no hay comentarios")
    void testListarComentariosVacios() {
        // Arrange
        when(comentarioRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<ComentarioDTO> resultados = comentarioService.listar();

        // Assert
        assertThat(resultados).isEmpty();
    }

    @Test
    @DisplayName("Debería obtener un comentario por su ID")
    void testObtenerPorId() {
        // Arrange
        when(comentarioRepository.findById(1)).thenReturn(Optional.of(comentario));

        // Act
        Optional<ComentarioDTO> resultado = comentarioService.obtenerPorId(1);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getIdComentario()).isEqualTo(1);
    }

    @Test
    @DisplayName("Debería actualizar un comentario existente")
    void testActualizarComentario() {
        // Arrange
        UsuarioInfoDTO usuarioInfo = new UsuarioInfoDTO();
        usuarioInfo.setIdUser(1);
        usuarioInfo.setUsername("testuser");
        CursoInfoDTO cursoInfo = new CursoInfoDTO();
        cursoInfo.setIdCurso(101);
        cursoInfo.setNombreCurso("Curso de Mockito");

        ComentarioDTO dtoActualizado = new ComentarioDTO();
        dtoActualizado.setComentario("Comentario actualizado");
        dtoActualizado.setCalificacion(4);
        dtoActualizado.setFecha(LocalDate.now());
        dtoActualizado.setUsuario(usuarioInfo);
        dtoActualizado.setCurso(cursoInfo);

        when(comentarioRepository.findById(1)).thenReturn(Optional.of(comentario));
        when(comentarioRepository.save(any(Comentario.class))).thenReturn(comentario);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(cursoRepository.findById(101)).thenReturn(Optional.of(curso));

        // Act
        Optional<ComentarioDTO> resultado = comentarioService.actualizar(1, dtoActualizado);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getComentario()).isEqualTo("Comentario actualizado");
        assertThat(resultado.get().getCalificacion()).isEqualTo(4);

        verify(comentarioRepository, times(1)).save(any(Comentario.class));
    }

    @Test
    @DisplayName("Debería eliminar un comentario si existe")
    void testEliminarComentario() {
        // Arrange
        when(comentarioRepository.existsById(1)).thenReturn(true);
        doNothing().when(comentarioRepository).deleteById(1);

        // Act
        boolean resultado = comentarioService.eliminar(1);

        // Assert
        assertThat(resultado).isTrue();
        verify(comentarioRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("No debería eliminar un comentario si no existe")
    void testEliminarComentarioNoExistente() {
        // Arrange
        when(comentarioRepository.existsById(99)).thenReturn(false);

        // Act
        boolean resultado = comentarioService.eliminar(99);

        // Assert
        assertThat(resultado).isFalse();
        verify(comentarioRepository, never()).deleteById(99);
    }

    @Test
    @DisplayName("Debería buscar comentarios por ID de curso")
    void testBuscarPorCurso() {
        // Arrange
        when(comentarioRepository.findByCursoIdCurso(101)).thenReturn(List.of(comentario));

        // Act
        List<ComentarioDTO> resultados = comentarioService.buscarPorCurso(101);

        // Assert
        assertThat(resultados).isNotEmpty();
        assertThat(resultados.get(0).getCurso().getIdCurso()).isEqualTo(101);
    }

    @Test
    @DisplayName("Debería buscar comentarios por ID de usuario")
    void testBuscarPorUsuario() {
        // Arrange
        when(comentarioRepository.findByUsuarioIdUser(1)).thenReturn(List.of(comentario));

        // Act
        List<ComentarioDTO> resultados = comentarioService.buscarPorUsuario(1);

        // Assert
        assertThat(resultados).isNotEmpty();
        assertThat(resultados.get(0).getUsuario().getIdUser()).isEqualTo(1);
    }
}