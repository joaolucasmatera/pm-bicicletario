package com.example.bicicletario.bicicletario.application;

import com.example.bicicletario.bicicletario.domain.Ciclista;
import com.example.bicicletario.bicicletario.domain.Cobranca;
import com.example.bicicletario.bicicletario.domain.dto.NovoCobrancaDTO;
import com.example.bicicletario.bicicletario.domain.enums.StatusCobranca;
import com.example.bicicletario.bicicletario.infraestructure.CobrancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class CobrancaService {


    @Autowired
    private RestTemplate restTemplate;

    // Simulação do repositório de cobrança e serviço de email
    @Autowired
    private CobrancaRepository cobrancaRepository;

    @Autowired
    private EmailService emailService;

    public Cobranca realizarCobranca(NovoCobrancaDTO novaCobranca) throws Exception {
        // Recuperar ciclista
        Ciclista ciclista = recuperarCiclista(novaCobranca.getCiclista());

        if (ciclista == null) {
            throw new Exception("Ciclista não encontrado");
        }

        // Criar nova cobrança
        Cobranca cobranca = new Cobranca();
        cobranca.setCiclista(novaCobranca.getCiclista());
        cobranca.setValor(novaCobranca.getValor());
        cobranca.setStatusCobranca(StatusCobranca.PENDENTE);
        cobranca.setHoraSolicitacao(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        // Simular envio para administradora de cartão de crédito
        boolean pagamentoConfirmado = enviarParaAdministradoraCC(cobranca);

        if (pagamentoConfirmado) {
            cobranca.setStatusCobranca(StatusCobranca.PAGA);
            cobranca.setHoraFinalizacao(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        } else {
            cobranca.setStatusCobranca(StatusCobranca.FALHA);
            // #TODO
            // Implementar enviar para a fila de cobrança
        }

        // Salvar cobrança
        cobranca = cobrancaRepository.save(cobranca);

        // Enviar email ao ciclista
        enviarEmailCobranca(ciclista, cobranca);

        return cobranca;
    }



    public Ciclista recuperarCiclista(int idCiclista) throws Exception {
        try {
            //Ciclista ciclista = restTemplate.getForObject("http://localhost:8080/ciclista/" + idCiclista, Ciclista.class);
            //Simulando ciclista
            Ciclista ciclista = new Ciclista();
            ciclista.setId(idCiclista);
            ciclista.setNome("Ciclista Simulado");
            ciclista.setEmail("simulado@exemplo.com");
            return ciclista;
        } catch (Exception e) {
            throw new Exception("Erro ao recuperar ciclista", e);
        }}

        private boolean enviarParaAdministradoraCC(Cobranca cobranca) {
            // Lógica de integração com administradora de cartão de crédito
            return true; // Simulação de sucesso
        }

    private void enviarEmailCobranca(Ciclista ciclista, Cobranca cobranca) {
        // Lógica para envio de email
        // emailService.enviarEmail(ciclista.getEmail(), "Detalhes da sua cobrança", "Detalhes da cobrança...");
    }
}

