package br.ufba.matc76.atividade1.controller;

import br.ufba.matc76.atividade1.model.Chamado;
import br.ufba.matc76.atividade1.model.Prioridade;
import br.ufba.matc76.atividade1.model.Status;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    private final List<Chamado> chamados = new ArrayList<>(
        List.of(
            new Chamado(
                1L,
                "Servidor de banco de dados inacessível",
                "Bruno Cardoso",
                "TI",
                Prioridade.ALTA,
                Status.ABERTO
            ),
            new Chamado(
                2L,
                "Queda do link de internet do escritório",
                "Camila Ribeiro",
                "TI",
                Prioridade.ALTA,
                Status.EM_ANDAMENTO
            ),
            new Chamado(
                3L,
                "Conta de administrador possivelmente comprometida",
                "Diego Martins",
                "TI",
                Prioridade.ALTA,
                Status.RESOLVIDO
            ),
            new Chamado(
                4L,
                "Impressora fiscal da recepção sem papel",
                "Elisa Prado",
                "Financeiro",
                Prioridade.MEDIA,
                Status.ABERTO
            ),
            new Chamado(
                5L,
                "Notebook com superaquecimento e desligamentos",
                "Felipe Andrade",
                "Desenvolvimento",
                Prioridade.MEDIA,
                Status.EM_ANDAMENTO
            ),
            new Chamado(
                6L,
                "Redefinição de senha do ERP financeiro",
                "Gabriela Costa",
                "Financeiro",
                Prioridade.MEDIA,
                Status.RESOLVIDO
            ),
            new Chamado(
                7L,
                "Troca de toner da impressora do RH",
                "Henrique Lopes",
                "RH",
                Prioridade.BAIXA,
                Status.ABERTO
            ),
            new Chamado(
                8L,
                "Wi-Fi instável na sala de reuniões",
                "Isabela Freitas",
                "Comercial",
                Prioridade.BAIXA,
                Status.EM_ANDAMENTO
            ),
            new Chamado(
                9L,
                "Instalação de segundo monitor",
                "Jonas Vieira",
                "Marketing",
                Prioridade.BAIXA,
                Status.RESOLVIDO
            ),
            new Chamado(
                10L,
                "Solicitação de acesso à VPN corporativa",
                "Karina Duarte",
                "Desenvolvimento",
                Prioridade.MEDIA,
                Status.ABERTO
            )
        )
    );

    @GetMapping({"", "/"})
    public List<Chamado> listarChamados(){
        return this.chamados;
    }

    @GetMapping("/resumo")
    public ResponseEntity<Map<String, Long>> resumo() {
        long abertos = chamados.stream()
                .filter(c -> c.status() == Status.ABERTO)
                .count();

        long emAndamento = chamados.stream()
                .filter(c -> c.status() == Status.EM_ANDAMENTO)
                .count();

        long resolvidos = chamados.stream()
                .filter(c -> c.status() == Status.RESOLVIDO)
                .count();

        Map<String, Long> resumo = new LinkedHashMap<>();
        resumo.put("abertos", abertos);
        resumo.put("emAndamento", emAndamento);
        resumo.put("resolvidos", resolvidos);
        resumo.put("total", (long) chamados.size());

        return ResponseEntity.ok(resumo);
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Chamado> chamadoPorId(@PathVariable long id){
        return chamados.stream()
            .filter(
                chamado -> 
                chamado.id() == id
            )
            .findFirst()
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/setor")
    public List<Chamado> buscarPorSetor(@RequestParam() String nome) {
        return chamados.stream()
            .filter(c -> c.setor().equalsIgnoreCase(nome))
            .toList();
    }

    @GetMapping("/prioridade")
    public List<Chamado> buscarPorPrioridade(@RequestParam() String nivel){
        Prioridade prioridade;

        try {
            prioridade = Prioridade.valueOf(nivel.toUpperCase());
        } catch (IllegalArgumentException e) {
            return List.of();
        }

        return chamados.stream()
            .filter(c -> c.prioridade() == prioridade)
            .toList();
    }

    @GetMapping("/busca")
    public List<Chamado> buscarPorTitulo(@RequestParam String texto) {
        String termo = texto.toLowerCase();
        return chamados.stream()
            .filter(c -> c.titulo().toLowerCase().contains(termo))
            .toList();
    }

    @GetMapping("/criticos")
    public List<Chamado> listarCriticos() {
        return chamados.stream()
            .filter(c -> c.prioridade() == Prioridade.ALTA && c.status() != Status.RESOLVIDO)
            .toList();
    }

    @PostMapping
    public ResponseEntity<Chamado> cadastrar(@RequestBody Chamado novo) {
        long proximoId = chamados.stream()
                .mapToLong(Chamado::id)
                .max()
                .orElse(0L) + 1L;

        Chamado criado = new Chamado(
                proximoId,
                novo.titulo(),
                novo.solicitante(),
                novo.setor(),
                novo.prioridade(),
                novo.status() != null ? novo.status() : Status.ABERTO
        );

        chamados.add(criado);

        return ResponseEntity
                .created(URI.create("/chamados/" + proximoId))
                .body(criado);
    }

}