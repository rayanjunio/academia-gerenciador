package br.com.academia.academia.scheduled;

import br.com.academia.academia.modelo.Mensalidade;
import br.com.academia.academia.repositorio.MensalidadeRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MensalidadeScheduled {

  @Autowired
  private MensalidadeRepositorio mensalidadeRepositorio;

  @Scheduled(fixedRate = 30000)
  private void decrementarMensalidade() {
    List<Mensalidade> mensalidades = mensalidadeRepositorio.findAll();
    
    for (Mensalidade mensalidade : mensalidades) {
      if (mensalidade.getDias() > 0) {
        mensalidade.setDias(mensalidade.getDias() - 1);
      } else {
        mensalidade.setMensalidadePaga(false);
      }
      mensalidadeRepositorio.save(mensalidade);
    }
  }
}
