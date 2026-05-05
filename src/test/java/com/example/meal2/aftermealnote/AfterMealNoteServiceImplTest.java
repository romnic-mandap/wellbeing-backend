package com.example.meal2.aftermealnote;

import com.example.meal2.aftermealnote.dto.AfterMealNoteCreationDTO;
import com.example.meal2.aftermealnote.dto.AfterMealNoteDetailedDTO;
import com.example.meal2.aftermealnote.dto.AfterMealNoteUpdateDTO;
import com.example.meal2.exception.NotResourceOwnerException;
import com.example.meal2.exception.ResourceLimitException;
import com.example.meal2.exception.ResourceNotFoundException;
import com.example.meal2.mealitem.MealItem;
import com.example.meal2.mealitem.MealItemRepository;
import com.example.meal2.mealitem.MealItemServiceImpl;
import com.example.meal2.user.Role;
import com.example.meal2.user.User;

import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AfterMealNoteServiceImplTest {

    @Mock
    private AfterMealNoteRepository afterMealNoteRepository;

    @Mock
    private MealItemServiceImpl mealItemService;

    @InjectMocks
    private AfterMealNoteServiceImpl afterMealNoteService;

    private User user;

    private MealItem mealItem;

    private Integer maxMealItemAfterMealNotes = 7;

    @BeforeEach
    public void setUp(){
        ReflectionTestUtils.setField(afterMealNoteService, "maxMealItemAfterMealNotes", maxMealItemAfterMealNotes);

        user = new User();
        user.setId(9999);
        user.setUsername("test_user");
        user.setPassword("password");
        user.setRole(Role.USER);

        mealItem = new MealItem();
        mealItem.setId(9999L);
        mealItem.setUserId(9999);
        mealItem.setMeal("standard meal");
        mealItem.setDate(LocalDate.parse("2023-06-09"));
        mealItem.setTime(LocalTime.parse("14:35:00"));
        mealItem.setMealSize(MealItem.MealSize.heavy);
        mealItem.setNote("test note");
    }

    @DisplayName("createAfterMealNote: normal")
    @Test
    void createAfterMealNote() {
        AfterMealNoteCreationDTO amncDTO = new AfterMealNoteCreationDTO(
                9999L,
                LocalDate.parse("2023-06-20"),
                LocalTime.parse("19:53:00"),
                "test note"
        );

        AfterMealNote amn = new AfterMealNote();
        amn.setId(1L);

        when(mealItemService.getMealItemById(any())).thenReturn(Optional.of(mealItem));
        when(afterMealNoteRepository.save(any(AfterMealNote.class))).thenReturn(amn);

        afterMealNoteService.createAfterMealNote(user, amncDTO);

        verify(afterMealNoteRepository, times(1)).save(any(AfterMealNote.class));
    }

    @DisplayName("createAfterMealNote: does not own resource")
    @Test
    void createAfterMealNote1() {
        mealItem.setUserId(9998);

        AfterMealNoteCreationDTO amncDTO = new AfterMealNoteCreationDTO(
                9999L,
                LocalDate.parse("2023-06-20"),
                LocalTime.parse("19:53:00"),
                "test note"
        );

        when(mealItemService.getMealItemById(any())).thenReturn(Optional.of(mealItem));

        Assertions.assertThrows(
                NotResourceOwnerException.class,
                () -> afterMealNoteService.createAfterMealNote(user, amncDTO)
        );
        verify(afterMealNoteRepository, never()).save(any(AfterMealNote.class));
    }
    @DisplayName("createAfterMealNote: mealitem id not found")
    @Test
    void createAfterMealNote2() {
        AfterMealNoteCreationDTO amncDTO = new AfterMealNoteCreationDTO(
                9998L,
                LocalDate.parse("2023-06-20"),
                LocalTime.parse("19:53:00"),
                "test note"
        );

        when(mealItemService.getMealItemById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> afterMealNoteService.createAfterMealNote(user, amncDTO)
        );
        verify(afterMealNoteRepository, never()).save(any(AfterMealNote.class));
    }
    @DisplayName("createAfterMealNote: max aftermealnotes")
    @Test
    void createAfterMealNote3(){
        AfterMealNoteCreationDTO amncDTO = new AfterMealNoteCreationDTO(
                9999L,
                LocalDate.parse("2023-06-20"),
                LocalTime.parse("19:53:00"),
                "test note"
        );
        mealItem.setAfterMealNotes(new HashSet<>());
        for(int i=0;i<maxMealItemAfterMealNotes;i++){
            AfterMealNote amn = new AfterMealNote();
            amn.setId(10L + i);
            amn.setMealItemId(9999L);
            amn.setTime(LocalTime.parse("19:53:00"));
            amn.setDate(LocalDate.parse("2023-06-20"));
            amn.setNote("test note");
            mealItem.getAfterMealNotes().add(amn);
        }

        AfterMealNote amn = new AfterMealNote();
        amn.setId(1L);

        when(mealItemService.getMealItemById(any())).thenReturn(Optional.of(mealItem));

        Assertions.assertThrows(
                ResourceLimitException.class,
                () -> afterMealNoteService.createAfterMealNote(user, amncDTO)
        );

        verify(afterMealNoteRepository, never()).save(any(AfterMealNote.class));
    }

    @DisplayName("getAfterMealNote: normal")
    @Test
    void getAfterMealNote() {
        AfterMealNote amn = new AfterMealNote();
        amn.setId(1L);
        amn.setMealItemId(9999L);
        amn.setDate(LocalDate.parse("2023-06-20"));
        amn.setTime(LocalTime.parse("19:53:00"));
        amn.setNote("test note");
        amn.setMealItem(mealItem);

        when(afterMealNoteRepository.findById(1L)).thenReturn(Optional.of(amn));

        AfterMealNoteDetailedDTO amndDTO = afterMealNoteService.getAfterMealNote(
                user, 1L
        );

        Assert.notNull(amndDTO);
        Assert.isInstanceOf(AfterMealNoteDetailedDTO.class, amndDTO);
    }
    @DisplayName("getAfterMealNote: does not own resource")
    @Test
    void getAfterMealNote1() {
        AfterMealNote amn = new AfterMealNote();
        amn.setId(1L);
        amn.setMealItemId(9999L);
        amn.setDate(LocalDate.parse("2023-06-20"));
        amn.setTime(LocalTime.parse("19:53:00"));
        amn.setNote("test note");
        amn.setMealItem(mealItem);
        user.setId(1);

        when(afterMealNoteRepository.findById(9999L)).thenReturn(Optional.of(amn));
        Assertions.assertThrows(
                NotResourceOwnerException.class,
                () -> afterMealNoteService.getAfterMealNote(user, 9999L)
        );
    }
    @DisplayName("getAfterMealNote: mealitem id not found")
    @Test
    void getAfterMealNote2() {
        when(afterMealNoteRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> afterMealNoteService.getAfterMealNote(user, 1L)
        );
    }

    @DisplayName("updateAfterMealNote: normal")
    @Test
    void updateAfterMealNote() {
        AfterMealNoteUpdateDTO amnuDTO = new AfterMealNoteUpdateDTO(
                LocalDate.parse("2023-06-20"), LocalTime.parse("19:53:00"), "test note"
        );
        AfterMealNote amn = new AfterMealNote();
        amn.setId(1L);
        amn.setMealItemId(9999L);
        amn.setDate(LocalDate.parse("2023-06-20"));
        amn.setTime(LocalTime.parse("19:53:00"));
        amn.setNote("test note");
        amn.setMealItem(mealItem);

        when(afterMealNoteRepository.findById(1L)).thenReturn(Optional.of(amn));
        afterMealNoteService.updateAfterMealNote(user, 1L, amnuDTO);
        verify(afterMealNoteRepository, times(1)).save(any(AfterMealNote.class));
    }
    @DisplayName("updateAfterMealNote: does not own this resource")
    @Test
    void updateAfterMealNote1() {
        AfterMealNoteUpdateDTO amnuDTO = new AfterMealNoteUpdateDTO(
                LocalDate.parse("2023-06-20"), LocalTime.parse("19:53:00"), "test note"
        );
        AfterMealNote amn = new AfterMealNote();
        amn.setId(1L);
        amn.setMealItemId(9999L);
        amn.setDate(LocalDate.parse("2023-06-20"));
        amn.setTime(LocalTime.parse("19:53:00"));
        amn.setNote("test note");
        amn.setMealItem(mealItem);
        user.setId(1);

        when(afterMealNoteRepository.findById(1L)).thenReturn(Optional.of(amn));
        Assertions.assertThrows(
                NotResourceOwnerException.class,
                () -> afterMealNoteService.updateAfterMealNote(user, 1L, amnuDTO)
        );
        verify(afterMealNoteRepository, never()).save(any(AfterMealNote.class));
    }
    @DisplayName("updateAfterMealNote: aftermealnote id not found")
    @Test
    void updateAfterMealNote2() {
        AfterMealNoteUpdateDTO amnuDTO = new AfterMealNoteUpdateDTO(
                LocalDate.parse("2023-06-20"), LocalTime.parse("19:53:00"), "test note"
        );
        when(afterMealNoteRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> afterMealNoteService.updateAfterMealNote(user, 1L, amnuDTO)
        );
        verify(afterMealNoteRepository, never()).save(any(AfterMealNote.class));
    }


    /*

    @Test
    void updateAfterMealNote() {
    }



     */

    @DisplayName("deleteAfterMealNote: normal")
    @Test
    void deleteAfterMealNote() {
        AfterMealNote amn = new AfterMealNote();
        amn.setId(1L);
        amn.setMealItemId(9999L);
        amn.setDate(LocalDate.parse("2023-06-20"));
        amn.setTime(LocalTime.parse("19:53:00"));
        amn.setNote("test note");
        amn.setMealItem(mealItem);

        when(afterMealNoteRepository.findById(1L)).thenReturn(Optional.of(amn));

        afterMealNoteService.deleteAfterMealNote(user, 1L);

        verify(afterMealNoteRepository, times(1)).deleteById(1L);
    }
    @DisplayName("deleteAfterMealNote: does not own this resource")
    @Test
    void deleteAfterMealNote1() {
        AfterMealNote amn = new AfterMealNote();
        amn.setId(1L);
        amn.setMealItemId(9999L);
        amn.setDate(LocalDate.parse("2023-06-20"));
        amn.setTime(LocalTime.parse("19:53:00"));
        amn.setNote("test note");
        amn.setMealItem(mealItem);
        user.setId(1);

        when(afterMealNoteRepository.findById(9999L)).thenReturn(Optional.of(amn));
        Assertions.assertThrows(
                NotResourceOwnerException.class,
                () -> afterMealNoteService.deleteAfterMealNote(user, 9999L)
        );
        verify(afterMealNoteRepository, never()).deleteById(1L);
    }
    @DisplayName("deleteAfterMealNote: aftermealnote id not found")
    @Test
    void deleteAfterMealNote2() {
        when(afterMealNoteRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> afterMealNoteService.deleteAfterMealNote(user, 1L)
        );
        verify(afterMealNoteRepository, never()).deleteById(1L);
    }
}