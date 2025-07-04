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

import static org.junit.jupiter.api.Assertions.*;
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

        // CORRECCIÓN 1: Se usa el constructor vacío y los setters para los DTOs.
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
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(cursoRepository.findById(101)).thenReturn(Optional.of(curso));
        when(comentarioRepository.save(any(Comentario.class))).thenReturn(comentario);

        ComentarioDTO resultado = comentarioService.guardar(comentarioDTO);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdComentario());
        assertEquals("testuser", resultado.getUsuario().getUsername());
        verify(comentarioRepository, times(1)).save(any(Comentario.class));
    }

    @Test
    @DisplayName("Debería lanzar EntityNotFoundException si el usuario no existe al guardar")
    void testGuardarComentarioUsuarioNoEncontrado() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());

        // CORRECCIÓN 2: Se simplifica la lambda de "statement" a "expression".
        assertThrows(EntityNotFoundException.class,
                () -> comentarioService.guardar(comentarioDTO),
                "Debería lanzar EntityNotFoundException"
        );

        verify(comentarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería listar todos los comentarios")
    void testListarComentarios() {
        when(comentarioRepository.findAll()).thenReturn(List.of(comentario));
        List<ComentarioDTO> resultados = comentarioService.listar();
        assertFalse(resultados.isEmpty());
        assertEquals(1, resultados.size());
    }

    @Test
    @DisplayName("Debería devolver una lista vacía si no hay comentarios")
    void testListarComentariosVacios() {
        when(comentarioRepository.findAll()).thenReturn(Collections.emptyList());
        List<ComentarioDTO> resultados = comentarioService.listar();
        assertTrue(resultados.isEmpty());
    }

    @Test
    @DisplayName("Debería obtener un comentario por su ID")
    void testObtenerPorId() {
        when(comentarioRepository.findById(1)).thenReturn(Optional.of(comentario));
        Optional<ComentarioDTO> resultado = comentarioService.obtenerPorId(1);
        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getIdComentario());
    }

    @Test
    @DisplayName("Debería actualizar un comentario existente")
    void testActualizarComentario() {
        // CORRECCIÓN 3: Se usa el constructor vacío y los setters también aquí.
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

        Optional<ComentarioDTO> resultado = comentarioService.actualizar(1, dtoActualizado);

        assertTrue(resultado.isPresent());
        verify(comentarioRepository, times(1)).save(any(Comentario.class));
    }

    @Test
    @DisplayName("Debería eliminar un comentario si existe")
    void testEliminarComentario() {
        when(comentarioRepository.existsById(1)).thenReturn(true);
        doNothing().when(comentarioRepository).deleteById(1);
        boolean resultado = comentarioService.eliminar(1);
        assertTrue(resultado);
        verify(comentarioRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("No debería eliminar un comentario si no existe")
    void testEliminarComentarioNoExistente() {
        when(comentarioRepository.existsById(99)).thenReturn(false);
        boolean resultado = comentarioService.eliminar(99);
        assertFalse(resultado);
        verify(comentarioRepository, never()).deleteById(99);
    }

    @Test
    @DisplayName("Debería buscar comentarios por ID de curso")
    void testBuscarPorCurso() {
        when(comentarioRepository.findByCursoIdCurso(101)).thenReturn(List.of(comentario));
        List<ComentarioDTO> resultados = comentarioService.buscarPorCurso(101);
        assertEquals(1, resultados.size());
        assertEquals(101, resultados.get(0).getCurso().getIdCurso());
    }

    @Test
    @DisplayName("Debería buscar comentarios por ID de usuario")
    void testBuscarPorUsuario() {
        when(comentarioRepository.findByUsuarioIdUser(1)).thenReturn(List.of(comentario));
        List<ComentarioDTO> resultados = comentarioService.buscarPorUsuario(1);
        assertEquals(1, resultados.size());
        assertEquals(1, resultados.get(0).getUsuario().getIdUser());
    }
}