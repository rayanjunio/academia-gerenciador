package br.com.academia.academia.servico;

import br.com.academia.academia.excecoes.InvalidInformationException;
import br.com.academia.academia.excecoes.NullIdentifierException;
import br.com.academia.academia.excecoes.PendingPaymentException;
import br.com.academia.academia.modelo.Aluno;
import br.com.academia.academia.modelo.Mensalidade;
import br.com.academia.academia.repositorio.AlunoRepositorio;
import br.com.academia.academia.repositorio.MensalidadeRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AlunoServicoTest {

  @InjectMocks
  AlunoServico alunoServico;

  @Mock
  AlunoRepositorio alunoRepositorio;

  @Mock
  MensalidadeRepositorio mensalidadeRepositorio;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("Should return gym member when their data is valid")
  void cadastrar_ShouldReturnGymMember_WhenDataGymMemberAreValid() {
    Aluno aluno = new Aluno("83207430015", "José", LocalDate.of(2003, 3, 17));
    aluno.setId(1);

    Mensalidade mensalidade = new Mensalidade();
    mensalidade.setId(2);

    when(mensalidadeRepositorio.save(any(Mensalidade.class))).thenReturn(mensalidade);

    when(alunoRepositorio.save(any(Aluno.class))).thenAnswer(invocation -> {
      Aluno savedAluno = invocation.getArgument(0);
      savedAluno.setMensalidade(mensalidade);
      return savedAluno;
    });

    Aluno alunoCadastrado = alunoServico.cadastrar(aluno);

    assertNotNull(alunoCadastrado.getMensalidade());
    assertEquals(alunoCadastrado.getDataNascimento(), aluno.getDataNascimento());
    assertEquals(alunoCadastrado.getCpf(), aluno.getCpf());
    assertEquals(alunoCadastrado.getName(), aluno.getName());
    assertEquals(alunoCadastrado.getMensalidade(), aluno.getMensalidade());

    verify(alunoRepositorio, times(1)).save(any(Aluno.class));
    verify(mensalidadeRepositorio, times(1)).save(any(Mensalidade.class));
  }

  @Test
  @DisplayName("Should throw an exception when name or date are invalid")
  void cadastrar_ShouldThrowException_WhenAnyGymMemberInformationIsInvalid() {
    Aluno aluno = new Aluno("83207430015", "", LocalDate.of(2003, 3, 17));
    aluno.setId(1);

    InvalidInformationException exception = assertThrows(InvalidInformationException.class,
            () -> alunoServico.cadastrar(aluno));
    assertEquals("Check the gym member data!", exception.getMessage());

    verify(mensalidadeRepositorio, times(0)).save(any(Mensalidade.class));
    verify(alunoRepositorio, times(0)).save(any(Aluno.class));
  }

  @Test
  @DisplayName("Should return the gym members list when there are gym members registered")
  void exibirTodos_ShouldReturnGymMembersList_WhenThereAreGymMembersRegistered() {
    Aluno firstAluno = new Aluno("83207430015", "José", LocalDate.of(2003, 3, 17));
    Aluno secondAluno = new Aluno("12009428099", "Alex", LocalDate.of(2005, 5, 9));

    List<Aluno> alunoList = List.of(firstAluno, secondAluno);

    when(alunoRepositorio.findAll()).thenReturn(alunoList);

    assertEquals(alunoList, alunoServico.exibirTodos());

    verify(alunoRepositorio, times(1)).findAll();
  }

  @Test
  @DisplayName("Should return an empty list when there are no gym members registered")
  void exibirTodos_ShouldReturnEmptyList_WhenThereAreNoGymMemberRegistered() {
    List<Aluno> alunoList = new ArrayList<>();

    when(alunoRepositorio.findAll()).thenReturn(alunoList);

    assertEquals(alunoList, alunoServico.exibirTodos());

    verify(alunoRepositorio, times(1)).findAll();
  }

  @Test
  @DisplayName("Should return gym members whose names contain typed characters")
  void exibirPorNome_ShouldReturnGymMembers_WhoseNamesContainTypedCharacters() {
    Aluno firstAluno = new Aluno("83207430015", "José", LocalDate.of(2003, 3, 17));
    Aluno secondAluno = new Aluno("12009428099", "João", LocalDate.of(2005, 5, 9));

    when(alunoRepositorio.findByNameContaining("Jo")).thenReturn(List.of(firstAluno, secondAluno));

    List<Aluno> alunoList = alunoServico.exibirPorNome("Jo");

    assertNotNull(alunoList);
    assertEquals(List.of(firstAluno, secondAluno), alunoList);

    verify(alunoRepositorio, times(1)).findByNameContaining(anyString());
  }

  @Test
  @DisplayName("Should throw an exception when no gym members have names containing typed characters")
  void exibirPorNome_ShouldThrowException_WhenNoGymMembersHaveNamesContainingTypedCharacters() {
    when(alunoRepositorio.findByNameContaining("Jo")).thenReturn(List.of());

    NullIdentifierException exception = assertThrows(NullIdentifierException.class,
            () -> alunoServico.exibirPorNome("Jo"));
    assertEquals("There is not gym members with those characters", exception.getMessage());

    verify(alunoRepositorio, times(1)).findByNameContaining(anyString());
  }

  @Test
  @DisplayName("Should return a success message when gym member code and payment is ok")
  void entrar_ShouldReturnOneSuccessMessage_WhenGymMemberDataIsValidAndPaymentIsOk() {
    Aluno aluno = new Aluno("83207430015", "José", LocalDate.of(2003, 3, 17));
    aluno.setId(2);

    Mensalidade mensalidade = new Mensalidade();
    aluno.setMensalidade(mensalidade);

    when(alunoRepositorio.findById(aluno.getId())).thenReturn(aluno);

    assertTrue(alunoServico.entrar(aluno.getId()).equalsIgnoreCase("Entrada autorizada"));

    verify(alunoRepositorio, times(1)).findById(anyLong());
  }

  @Test
  @DisplayName("Should throw an exception when gym member id does not exist")
  void entrar_ShouldThrowException_WhenGymMemberIdDoesNotExist() {
    when(alunoRepositorio.findById(3)).thenReturn(null);

    NullIdentifierException exception = assertThrows(NullIdentifierException.class,
            () -> alunoServico.entrar(3));
    assertEquals("O identificador digitado é nulo.", exception.getMessage());

    verify(alunoRepositorio, times(1)).findById(anyLong());
  }

  @Test
  @DisplayName("Should throw an exception when gym member payment is pending")
  void entrar_ShouldThrowException_WhenGymMemberPaymentIsPending() {
    Aluno aluno = new Aluno("83207430015", "José", LocalDate.of(2003, 3, 17));
    aluno.setId(2);

    Mensalidade mensalidade = new Mensalidade();
    mensalidade.setMensalidadePaga(false);

    aluno.setMensalidade(mensalidade);

    when(alunoRepositorio.findById(aluno.getId())).thenReturn(aluno);

    PendingPaymentException exception = assertThrows(PendingPaymentException.class,
            () -> alunoServico.entrar(aluno.getId()));
    assertEquals("Entrada não autorizada por pendência de pagamento!", exception.getMessage());

    verify(alunoRepositorio, times(1)).findById(anyLong());
  }

  @Test
  @DisplayName("Should return success message when gym member code is valid")
  void pagar_ShouldReturnSuccessMessage_WhenGymMemberCodeIsValid() {
    Aluno aluno = new Aluno("83207430015", "José", LocalDate.of(2003, 3, 17));
    aluno.setId(2);

    Mensalidade mensalidade = new Mensalidade();
    mensalidade.setMensalidadePaga(false);

    aluno.setMensalidade(mensalidade);

    when(alunoRepositorio.findById(aluno.getId())).thenReturn(aluno);

    assertTrue(alunoServico.pagar(aluno.getId()).equalsIgnoreCase("Mensalidade paga com sucesso!"));

    verify(mensalidadeRepositorio, times(1)).save(any(Mensalidade.class));
  }

  @Test
  @DisplayName("Should throw an exception when gym member code is invalid")
  void pagar_ShouldThrowException_WhenGymMemberCodeIsInvalid() {
    when(alunoRepositorio.findById(3)).thenReturn(null);

    NullIdentifierException exception = assertThrows(NullIdentifierException.class,
            () -> alunoServico.pagar(3));
    assertEquals("O identificador digitado é nulo.", exception.getMessage());

    verify(mensalidadeRepositorio, times(0)).save(any(Mensalidade.class));
  }

  @Test
  @DisplayName("Should return success message when gym member code is valid")
  void deletar_ShouldReturnSuccessMessage_WhenGymMemberCodeIsValid() {
    Aluno aluno = new Aluno("83207430015", "José", LocalDate.of(2003, 3, 17));
    aluno.setId(2);

    Mensalidade mensalidade = new Mensalidade();
    aluno.setMensalidade(mensalidade);

    when(alunoRepositorio.findById(aluno.getId())).thenReturn(aluno);

    assertTrue(alunoServico.deletar(aluno.getId()).equalsIgnoreCase("Aluno deletado com sucesso!"));

    verify(alunoRepositorio, times(1)).delete(aluno);
    verify(mensalidadeRepositorio, times(1)).delete(mensalidade);
  }

  @Test
  @DisplayName("Should throw an exception when gym member code is invalid")
  void deletar_ShouldThrowException_WhenGymMemberCodeIsInvalid() {
    when(alunoRepositorio.findById(3)).thenReturn(null);

    NullIdentifierException exception = assertThrows(NullIdentifierException.class,
            () -> alunoServico.deletar(3));
    assertEquals("O identificador digitado é nulo.", exception.getMessage());

    verify(alunoRepositorio, times(0)).delete(any());
    verify(mensalidadeRepositorio, times(0)).delete(any());
  }
}