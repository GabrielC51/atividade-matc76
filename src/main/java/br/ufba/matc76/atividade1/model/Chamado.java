package br.ufba.matc76.atividade1.model;

import br.ufba.matc76.atividade1.model.Prioridade;
import br.ufba.matc76.atividade1.model.Status;

public record Chamado(
    long id,
    String titulo,
    String solicitante,
    String setor,
    Prioridade prioridade,
    Status status
){}