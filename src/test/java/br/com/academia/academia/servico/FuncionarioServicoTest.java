package br.com.academia.academia.servico;

import br.com.academia.academia.excecoes.InvalidInformationException;
import br.com.academia.academia.excecoes.NullIdentifierException;
import br.com.academia.academia.modelo.Funcionario;
import br.com.academia.academia.modelo.MensalidadeFuncionario;
import br.com.academia.academia.repositorio.FuncionarioRepositorio;
import br.com.academia.academia.repositorio.MensalidadeFuncionarioRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FuncionarioServicoTest {

  @InjectMocks
  FuncionarioServico funcionarioServico;

  @Mock
  FuncionarioRepositorio funcionarioRepositorio;

  @Mock
  MensalidadeFuncionarioRepositorio mensalidadeRepositorio;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("Should return the registered employee when data is valid")
  void cadastrar_ShouldReturnEmployeeAfterHasBeenCreated() {
    Funcionario funcionario = new Funcionario("José", "Instrutor", "66372777037");
    funcionario.setId(2);

    MensalidadeFuncionario mensalidade = new MensalidadeFuncionario();
    mensalidade.setId(5);

    funcionario.setMensalidadeFuncionario(mensalidade);

    when(mensalidadeRepositorio.save(mensalidade)).thenReturn(mensalidade);

    when(funcionarioRepositorio.save(funcionario)).thenAnswer(invocation -> {
      Funcionario funcionarioSaved = invocation.getArgument(0);
      funcionarioSaved.setMensalidadeFuncionario(mensalidade);
      return funcionarioSaved;
    });

    Funcionario funcionarioCadastrado = funcionarioServico.cadastrar(funcionario);

    assertNotNull(funcionarioCadastrado.getMensalidadeFuncionario());
    assertEquals(funcionarioCadastrado.getId(), funcionario.getId());
    assertEquals(funcionarioCadastrado.getName(), funcionario.getName());
    assertEquals(funcionarioCadastrado.getCargo(), funcionario.getCargo());
    assertEquals(funcionarioCadastrado.getCpf(), funcionario.getCpf());

    verify(funcionarioRepositorio, times(1)).save(any(Funcionario.class));
    verify(mensalidadeRepositorio, times(1)).save(any(MensalidadeFuncionario.class));
  }

  @Test
  @DisplayName("Should throw an exception when any data is invalid")
  void cadastrar_ShouldThrowExceptionWhenEmployeeInformationIsInvalid() {
    Funcionario funcionario = new Funcionario("", "Instrutor", "66372777037");
    funcionario.setId(1);

    InvalidInformationException exception = assertThrows(InvalidInformationException.class,
            () -> funcionarioServico.cadastrar(funcionario));
    assertEquals("Check the employee data", exception.getMessage());

    verify(mensalidadeRepositorio, times(0)).save(any(MensalidadeFuncionario.class));
    verify(funcionarioRepositorio, times(0)).save(any(Funcionario.class));
  }

  @Test
  @DisplayName("Should return an employee list when there are employees registered")
  void exibirFuncionarios_ShouldReturnEmployeesListWhenThereAreEmployeesRegistered() {
    Funcionario fisrtFuncionario = new Funcionario("Julio", "Instrutor", "83207430015");
    Funcionario secondFuncionario = new Funcionario("Adalberto", "recepcionista", "66372777037");

    List<Funcionario> funcionarioList = List.of(fisrtFuncionario, secondFuncionario);

    when(funcionarioRepositorio.findAll()).thenReturn(funcionarioList);

    assertEquals(funcionarioList, funcionarioServico.exibirFuncionarios());

    verify(funcionarioRepositorio, times(1)).findAll();
  }

  @Test
  @DisplayName("Should return an empty list when there are no employees registered")
  void exibirFuncionarios_ShouldReturnEmptyListWhenThereAreNoEmployeesRegistered() {
    List<Funcionario> funcionarioList = new ArrayList<>();

    when(funcionarioRepositorio.findAll()).thenReturn(funcionarioList);

    assertEquals(funcionarioList, funcionarioServico.exibirFuncionarios());

    verify(funcionarioRepositorio, times(1)).findAll();
  }

  @Test
  @DisplayName("Should return employees whose names containing typed characters")
  void exibirPorNome_ShouldReturnEmployees_WhoseNamesContainingTypedCharacters() {
    Funcionario fisrtFuncionario = new Funcionario("João", "Instrutor", "83207430015");
    Funcionario secondFuncionario = new Funcionario("José", "recepcionista", "66372777037");

    when(funcionarioRepositorio.findByNameContaining("Jo")).thenReturn(List.of(fisrtFuncionario, secondFuncionario));

    List<Funcionario> returnedList = funcionarioServico.exibirPorNome("Jo");

    assertNotNull(returnedList);
    assertEquals(List.of(fisrtFuncionario, secondFuncionario), returnedList);

    verify(funcionarioRepositorio, times(1)).findByNameContaining(anyString());
  }

  @Test
  @DisplayName("Should throw an exception when no employees have names containing typed characters")
  void exibirPorNome_ShouldThrowException_WhenNoEmployeesHaveNamesContainingTypedCharacters() {
    List<Funcionario> funcionarioList = new ArrayList<>();

    when(funcionarioRepositorio.findByNameContaining("Jo")).thenReturn(funcionarioList);

    NullIdentifierException exception = assertThrows(NullIdentifierException.class,
            () -> funcionarioServico.exibirPorNome("Jo"));
    assertEquals("There is not employees with this name", exception.getMessage());

    verify(funcionarioRepositorio, times(1)).findByNameContaining(anyString());
  }

  @Test
  @DisplayName("Should return one success message when employee code exists")
  void entrar_ShouldReturnOneSuccessMessage_WhenEmployeeDataIsOk() {
    Funcionario funcionario = new Funcionario("João", "Instrutor", "83207430015");
    funcionario.setId(3);

    MensalidadeFuncionario mensalidade = new MensalidadeFuncionario();
    funcionario.setMensalidadeFuncionario(mensalidade);

    when(funcionarioRepositorio.findById(funcionario.getId())).thenReturn(funcionario);

    assertTrue(funcionarioServico.entrar(funcionario.getId()).equalsIgnoreCase("Entrada autorizada"));

    verify(funcionarioRepositorio, times(1)).findById(anyLong());
  }

  @Test
  @DisplayName("Should throw an exception when code employee does not exist")
  void entrar_ShouldThrowException_WhenCodeEmployeeIsInvalid() {
    when(funcionarioRepositorio.findById(anyLong())).thenReturn(null);

    NullIdentifierException exception = assertThrows(NullIdentifierException.class,
            () -> funcionarioServico.entrar(anyLong()));
    assertEquals("O identificador digitado é nulo.", exception.getMessage());

    verify(funcionarioRepositorio, times(1)).findById(anyLong());
  }

  @Test
  @DisplayName("Should return the updated employee when code and profession are valid")
  void mudarCargo_ShouldReturnTheUpdatedEmployee_WhenCodeAndProfessionAreValid() {
    Funcionario funcionario = new Funcionario("João", "Instrutor", "83207430015");
    funcionario.setId(3);

    when(funcionarioRepositorio.findById(anyLong())).thenReturn(funcionario);

    funcionario.setCargo("Recepcionista");

    assertEquals(funcionario.getCargo(), funcionarioServico.mudarCargo(anyLong(), "Recepcionista").getCargo());

    verify(funcionarioRepositorio, times(1)).findById(anyLong());
    verify(funcionarioRepositorio, times(1)).save(any(Funcionario.class));
  }

  @Test
  @DisplayName("Should throw an exception when code employee is invalid")
  void mudarCargo_ShouldThrowException_WhenCodeEmployeeIsInvalid() {
    when(funcionarioRepositorio.findById(anyLong())).thenReturn(null);

    NullIdentifierException exception = assertThrows(NullIdentifierException.class,
            () -> funcionarioServico.mudarCargo(3, "Instrutor"));
    assertEquals("O identificador digitado é nulo.", exception.getMessage());

    verify(funcionarioRepositorio, times(1)).findById(anyLong());
    verify(funcionarioRepositorio, times(0)).save(any(Funcionario.class));
  }

  @Test
  @DisplayName("")
  void demitir_ShouldReturnOneSuccessMessage_WhenEmployeeCodeIsValid() {
    Funcionario funcionario = new Funcionario("João", "Instrutor", "83207430015");
    funcionario.setId(3);

    MensalidadeFuncionario mensalidade = new MensalidadeFuncionario();
    mensalidade.setId(2);

    funcionario.setMensalidadeFuncionario(mensalidade);

    when(funcionarioRepositorio.findById(anyLong())).thenReturn(funcionario);

    assertEquals("Funcionário demitido com sucesso!", funcionarioServico.demitir(funcionario.getId()));

    verify(funcionarioRepositorio, times(1)).delete(any(Funcionario.class));
    verify(mensalidadeRepositorio, times(1)).delete(any(MensalidadeFuncionario.class));
  }
}
