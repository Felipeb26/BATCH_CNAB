package com.batsworks.batch.service.impl;

import com.batsworks.batch.config.exception.CnabProcessingException;
import com.batsworks.batch.domain.entity.BoletoAlteracao;
import com.batsworks.batch.domain.enums.TipoOCorrencia;
import com.batsworks.batch.domain.records.Cnab;
import com.batsworks.batch.repository.BoletoAlteracaoRepository;
import com.batsworks.batch.repository.CnabRepository;
import com.batsworks.batch.service.CnabService;
import com.batsworks.batch.utils.Converts;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.World;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.isNull;

/**
 * Está sendo retornado apenas false para não ser incluido
 * um registro que deveria ser para atualizacao não inclusão
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CnabServiceImpl implements CnabService {

    private final CnabRepository cnabRepository;
    private final BoletoAlteracaoRepository boletoAlteracaoRepository;
    private final Converts converts;

    @Override
    public Boolean ocorrencia02(Cnab cnab) throws Exception {
        var cnabEntity = cnabRepository.findByCnabNumeroTituloLike(cnab.numeroDocumento());
        if (isNull(cnabEntity))
            throw new CnabProcessingException("Não foi encontrado boleto para alteração", cnab.linha());

        var objAtual = converts.convertToHashMap(cnabEntity);
        var objAtualizado = converts.convertToHashMap(cnab);

        var alteracoes = converts.alteracoesEncontradas(objAtual, objAtualizado);
        var continua = converts.igualdades(objAtual, objAtualizado);

        log.info("{} \n", continua);
        log.info("{} \n", alteracoes);


        mapper.map
        cnabRepository.save(cnab);
        var boletoAlteracao = BoletoAlteracao.builder()
                .arquivo(cnab.arquivo())
                .boletoAlterado(cnabEntity)
                .camposALterados(alteracoes)
                .tipoDeAlteracao(TipoOCorrencia.PEDIDO_DE_BAIXA.name())
                .build();
        boletoAlteracaoRepository.save(boletoAlteracao);

        return false;
    }

    @Override
    public Boolean ocorrencia03(Cnab cnab) {
        return false;
    }

    @Override
    public Boolean ocorrencia04(Cnab cnab) {
        return false;
    }

    @Override
    public Boolean ocorrencia05(Cnab cnab) {
        return false;
    }

    @Override
    public Boolean ocorrencia06(Cnab cnab) {
        return false;
    }

}
