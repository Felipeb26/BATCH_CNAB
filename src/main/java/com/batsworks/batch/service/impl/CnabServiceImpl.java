package com.batsworks.batch.service.impl;

import com.batsworks.batch.config.exception.CnabProcessingException;
import com.batsworks.batch.domain.entity.BoletoAlteracao;
import com.batsworks.batch.domain.entity.CnabEntity;
import com.batsworks.batch.domain.enums.SituacaoCnab;
import com.batsworks.batch.domain.enums.TipoOCorrencia;
import com.batsworks.batch.domain.records.Cnab;
import com.batsworks.batch.repository.BoletoAlteracaoRepository;
import com.batsworks.batch.repository.CnabRepository;
import com.batsworks.batch.service.CnabService;
import com.batsworks.batch.utils.Converts;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

/**
 * Está sendo retornado apenas false para não ser incluido
 * um registro que deveria ser para atualizacao não inclusão
 */

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor
public class CnabServiceImpl implements CnabService {

    private final CnabRepository cnabRepository;
    private final BoletoAlteracaoRepository boletoAlteracaoRepository;
    private final Converts converts;
    private final ObjectMapper mapper;
    private static final String NOT_FOUND_BOLETO = "Não foi encontrado boleto para alteração";

    @Override
    public void ocorrencia02(Cnab cnab)  throws CnabProcessingException {
        var cnabEntity = cnabRepository.findByCnabNumeroTituloLike(cnab.numeroDocumento());
        if (isNull(cnabEntity)) throw new CnabProcessingException(NOT_FOUND_BOLETO, cnab.linha());

        cnabEntity.setSituacao(SituacaoCnab.AGUARDANDO_BAIXA);
        var boletoAlteracao = BoletoAlteracao.builder().camposAlterados(null).camposAntigos(null).tipoDeAlteracao(TipoOCorrencia.PEDIDO_DE_BAIXA.name()).boletoAlterado(cnabEntity).arquivo(cnab.arquivo()).build();
        boletoAlteracaoRepository.save(boletoAlteracao);
    }

    @Override
    public void ocorrencia04(Cnab cnab)  throws CnabProcessingException, IllegalAccessException {
        var cnabEntity = cnabRepository.findByCnabNumeroTituloLike(cnab.numeroDocumento());
        if (isNull(cnabEntity)) throw new CnabProcessingException(NOT_FOUND_BOLETO, cnab.linha());

        var objAtual = converts.convertToHashMap(cnabEntity);
        var objAtualizado = converts.convertToHashMap(cnab);

        var alteracoes = converts.alteracoes(objAtual, objAtualizado);
        removeEntityPattern(alteracoes);
        var continua = converts.camposAntigos(objAtual, alteracoes);

        var cnabEntityMapped = mapper.convertValue(cnab, CnabEntity.class);
        cnabEntityMapped.setId(cnabEntity.getId());
        cnabEntityMapped.setArquivo(cnabEntity.getArquivo());
        cnabEntityMapped.setSituacao(SituacaoCnab.AGUARDANDO_ALTERACAO);
        cnabRepository.save(cnabEntityMapped);

        var boletoAlteracao = BoletoAlteracao.builder().camposAlterados(alteracoes.isEmpty() ? null : alteracoes.toString()).camposAntigos(alteracoes.isEmpty() ? null : continua.toString()).tipoDeAlteracao(TipoOCorrencia.CONCESSAO_DE_ABATIMENTO.name()).boletoAlterado(cnabEntity).arquivo(cnab.arquivo()).build();
        boletoAlteracaoRepository.save(boletoAlteracao);
    }

    @Override
    public void ocorrencia05(Cnab cnab)  throws CnabProcessingException, IllegalAccessException {
        var cnabEntity = cnabRepository.findByCnabNumeroTituloLike(cnab.numeroDocumento());
        if (isNull(cnabEntity)) throw new CnabProcessingException(NOT_FOUND_BOLETO, cnab.linha());

        var objAtual = converts.convertToHashMap(cnabEntity);
        var objAtualizado = converts.convertToHashMap(cnab);

        var alteracoes = converts.alteracoes(objAtual, objAtualizado);
        removeEntityPattern(alteracoes);
        var continua = converts.camposAntigos(objAtual, alteracoes);

        var cnabEntityMapped = mapper.convertValue(cnab, CnabEntity.class);
        cnabEntityMapped.setId(cnabEntity.getId());
        cnabEntityMapped.setArquivo(cnabEntity.getArquivo());
        cnabEntityMapped.setSituacao(SituacaoCnab.AGUARDANDO_ALTERACAO);
        cnabRepository.save(cnabEntityMapped);

        var boletoAlteracao = BoletoAlteracao.builder().camposAlterados(alteracoes.isEmpty() ? null : alteracoes.toString()).camposAntigos(alteracoes.isEmpty() ? null : continua.toString()).tipoDeAlteracao(TipoOCorrencia.CANCELAMENTO_DE_ABATIMENTO_CONCEDIDO.name()).boletoAlterado(cnabEntity).arquivo(cnab.arquivo()).build();
        boletoAlteracaoRepository.save(boletoAlteracao);
    }

    @Override
    public void ocorrencia06(Cnab cnab)  throws CnabProcessingException, IllegalAccessException {
        var cnabEntity = cnabRepository.findByCnabNumeroTituloLike(cnab.numeroDocumento());
        if (isNull(cnabEntity)) throw new CnabProcessingException(NOT_FOUND_BOLETO, cnab.linha());

        var objAtual = converts.convertToHashMap(cnabEntity);
        var objAtualizado = converts.convertToHashMap(cnab);

        var alteracoes = converts.alteracoes(objAtual, objAtualizado);
        removeEntityPattern(alteracoes);
        var continua = converts.camposAntigos(objAtual, alteracoes);

        var cnabEntityMapped = mapper.convertValue(cnab, CnabEntity.class);
        cnabEntityMapped.setId(cnabEntity.getId());
        cnabEntityMapped.setArquivo(cnabEntity.getArquivo());
        cnabEntityMapped.setSituacao(SituacaoCnab.AGUARDANDO_ALTERACAO);
        cnabRepository.save(cnabEntityMapped);

        var boletoAlteracao = BoletoAlteracao.builder()
                .camposAlterados(alteracoes.isEmpty() ? null : alteracoes.toString())
                .camposAntigos(alteracoes.isEmpty() ? null : continua.toString())
                .tipoDeAlteracao(TipoOCorrencia.ALTERACAO_DO_VENCIMENTO.name())
                .boletoAlterado(cnabEntity)
                .arquivo(cnab.arquivo()).build();
        boletoAlteracaoRepository.save(boletoAlteracao);
    }

    @Override
    public void ocorrencia07(Cnab cnab)  throws CnabProcessingException, IllegalAccessException {
        var cnabEntity = cnabRepository.findByCnabNumeroTituloLike(cnab.numeroDocumento());
        if (isNull(cnabEntity)) throw new CnabProcessingException(NOT_FOUND_BOLETO, cnab.linha());

        var objAtual = converts.convertToHashMap(cnabEntity);
        var objAtualizado = converts.convertToHashMap(cnab);

        var alteracoes = converts.alteracoes(objAtual, objAtualizado);
        removeEntityPattern(alteracoes);
        var continua = converts.camposAntigos(objAtual, alteracoes);


        var cnabEntityMapped = mapper.convertValue(cnab, CnabEntity.class);
        cnabEntityMapped.setId(cnabEntity.getId());
        cnabEntityMapped.setArquivo(cnabEntity.getArquivo());
        cnabRepository.save(cnabEntityMapped);

        var boletoAlteracao = BoletoAlteracao.builder()
                .camposAlterados(alteracoes.isEmpty() ? null : alteracoes.toString())
                .camposAntigos(alteracoes.isEmpty() ? null : continua.toString())
                .tipoDeAlteracao(TipoOCorrencia.ALTERACAO_DO_CONTROLE_DO_PARTICIPANTE.name())
                .boletoAlterado(cnabEntity)
                .arquivo(cnab.arquivo()).build();
        boletoAlteracaoRepository.save(boletoAlteracao);
    }

    @Override
    public void ocorrencia08(Cnab cnab)  throws CnabProcessingException, IllegalAccessException {
        var cnabEntity = cnabRepository.findByCnabNumeroTituloLike(cnab.numeroDocumento());
        if (isNull(cnabEntity)) throw new CnabProcessingException(NOT_FOUND_BOLETO, cnab.linha());

        var objAtual = converts.convertToHashMap(cnabEntity);
        var objAtualizado = converts.convertToHashMap(cnab);

        var alteracoes = converts.alteracoes(objAtual, objAtualizado);
        removeEntityPattern(alteracoes);
        var continua = converts.camposAntigos(objAtual, alteracoes);


        var cnabEntityMapped = mapper.convertValue(cnab, CnabEntity.class);
        cnabEntityMapped.setId(cnabEntity.getId());
        cnabEntityMapped.setArquivo(cnabEntity.getArquivo());
        cnabRepository.save(cnabEntityMapped);

        var boletoAlteracao = BoletoAlteracao.builder()
                .camposAlterados(alteracoes.isEmpty() ? null : alteracoes.toString())
                .camposAntigos(alteracoes.isEmpty() ? null : continua.toString())
                .tipoDeAlteracao(TipoOCorrencia.ALTERACAO_DE_SEU_NUMERO.name())
                .boletoAlterado(cnabEntity)
                .arquivo(cnab.arquivo()).build();
        boletoAlteracaoRepository.save(boletoAlteracao);
    }

    @Override
    public void ocorrencia20(Cnab cnab) throws CnabProcessingException, IllegalAccessException {
        var cnabEntity = cnabRepository.findByCnabNumeroTituloLike(cnab.numeroDocumento());
        if (isNull(cnabEntity)) throw new CnabProcessingException(NOT_FOUND_BOLETO, cnab.linha());

        var objAtual = converts.convertToHashMap(cnabEntity);
        var objAtualizado = converts.convertToHashMap(cnab);

        var alteracoes = converts.alteracoes(objAtual, objAtualizado);
        removeEntityPattern(alteracoes);
        var continua = converts.camposAntigos(objAtual, alteracoes);

        var cnabEntityMapped = mapper.convertValue(cnab, CnabEntity.class);
        cnabEntityMapped.setId(cnabEntity.getId());
        cnabEntityMapped.setArquivo(cnabEntity.getArquivo());
        cnabRepository.save(cnabEntityMapped);

        var boletoAlteracao = BoletoAlteracao.builder()
                .camposAlterados(alteracoes.isEmpty() ? null : alteracoes.toString())
                .camposAntigos(alteracoes.isEmpty() ? null : continua.toString())
                .tipoDeAlteracao(TipoOCorrencia.ALTERACAO_DE_VALOR.name())
                .boletoAlterado(cnabEntity)
                .arquivo(cnab.arquivo()).build();
        boletoAlteracaoRepository.save(boletoAlteracao);
    }


    private void removeEntityPattern(Map<String, Object> alteracoes) {
        log.trace("ANTES: {}", alteracoes);
        var removeDefaultItens = List.of("id", "dataCadastro", "dataAtualizacao", "situacao", "arquivo", "observacao", "linha");
        removeDefaultItens.forEach(alteracoes::remove);
        log.trace("DEPOIS: {}", alteracoes);
    }

}
