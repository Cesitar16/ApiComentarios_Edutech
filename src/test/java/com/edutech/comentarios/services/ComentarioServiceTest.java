package com.edutech.comentarios.services;

import com.edutech.comentarios.dto.ComentarioDTO;
import com.edutech.comentarios.dto.CursoInfoDTO;
import com.edutech.comentarios.dto.UsuarioInfoDTO;
import com.edutech.comentarios.models.Comentario;
import com.edutech.comentarios.models.Curso;
import com.edutech.comentarios.models.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ComentarioServiceTest {

    @Test
    @DisplayName("Prueba para verificar la creación de un Comentario y sus relaciones")
    void testCrearComentarioConRelaciones() {
        // 1. Crear los objetos de dependencia (modelos)
        Usuario usuario = new Usuario();
        usuario.setIdUser(1);
        usuario.setUsername("testuser");

        Curso curso = new Curso();
        curso.setIdCurso(101);
        curso.setNombreCurso("Curso de Spring Boot");

        // 2. Crear el objeto principal (modelo Comentario)
        Comentario comentario = new Comentario();
        comentario.setIdComentario(1);
        comentario.setComentario("¡Excelente curso!");
        comentario.setCalificacion(5);
        comentario.setFecha(LocalDate.now());
        comentario.setUsuario(usuario);
        comentario.setCurso(curso);

        // 3. Validar que el objeto Comentario y sus relaciones se crearon correctamente
        assertNotNull(comentario);
        assertEquals(1, comentario.getIdComentario());
        assertEquals("¡Excelente curso!", comentario.getComentario());
        assertEquals(5, comentario.getCalificacion());
        assertEquals("testuser", comentario.getUsuario().getUsername());
        assertEquals("Curso de Spring Boot", comentario.getCurso().getNombreCurso());
    }

    @Test
    @DisplayName("Prueba para verificar la creación de un ComentarioDTO")
    void testCrearComentarioDTO() {
        // 1. Crear los DTOs de dependencia
        UsuarioInfoDTO usuarioInfo = new UsuarioInfoDTO();
        usuarioInfo.setIdUser(2);
        usuarioInfo.setUsername("dto_user");

        CursoInfoDTO cursoInfo = new CursoInfoDTO();
        cursoInfo.setIdCurso(202);
        cursoInfo.setNombreCurso("Curso de DTOs");
        
        // 2. Crear el DTO principal
        ComentarioDTO comentarioDTO = new ComentarioDTO();
        comentarioDTO.setIdComentario(2);
        comentarioDTO.setComentario("DTO creado correctamente");
        comentarioDTO.setCalificacion(4);
        comentarioDTO.setFecha(LocalDate.of(2025, 1, 15));
        comentarioDTO.setUsuario(usuarioInfo);
        comentarioDTO.setCurso(cursoInfo);

        // 3. Validar que el DTO y sus objetos anidados se crearon correctamente
        assertNotNull(comentarioDTO);
        assertEquals(2, comentarioDTO.getIdComentario());
        assertEquals(4, comentarioDTO.getCalificacion());
        assertEquals("dto_user", comentarioDTO.getUsuario().getUsername());
        assertEquals("Curso de DTOs", comentarioDTO.getCurso().getNombreCurso());
        assertEquals(LocalDate.of(2025, 1, 15), comentarioDTO.getFecha());
    }
}
