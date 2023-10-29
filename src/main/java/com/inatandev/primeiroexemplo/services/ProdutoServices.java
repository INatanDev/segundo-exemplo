package com.inatandev.primeiroexemplo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inatandev.primeiroexemplo.model.Produto;
import com.inatandev.primeiroexemplo.model.exception.ResourceNotFoundException;
import com.inatandev.primeiroexemplo.repository.ProdutoRepository;
import com.inatandev.primeiroexemplo.shared.ProdutoDTO;

@Service
public class ProdutoServices {
    
    @Autowired
    private ProdutoRepository produtoRepository;

    /**
     * Metodo para retornar uma lista de produtos
     * @return Lista de produtos.
     */
    public List<ProdutoDTO> obterTodos(){

        List<Produto> produtos = produtoRepository.findAll();

        return produtos.stream()
            .map(produto -> new ModelMapper().map(produto, ProdutoDTO.class))
            .collect(Collectors.toList());
    }

    /**
     * Metodo que retorna o produto encontrado pelo seu Id.
     * @param id do produto que sera localizado
     * @return Retorna um produto caso seja encontrado.
     */
    public Optional<ProdutoDTO> obterPorId(Integer id){
        //obtendo optional de produto pelo id.
        Optional<Produto> produto = produtoRepository.findById(id);

        // se nao encontrar vai lancar exceptional
        if(produto.isEmpty()){
            throw new ResourceNotFoundException("Produto com id: " + id + " nao encontrado");
        }

        //convertendo o optional de produto em um produtoDTO
        ProdutoDTO dto = new ModelMapper()
            .map(produto.get(), ProdutoDTO.class);

        //criando e retornando um optional de dto.    
        return Optional.of(dto);    
    }

    /**
     * Metodo para adicionar produto na lista.
     * @param produto que sera adicionado.
     * @return produto que foi adicionado na lista.
     */
    public ProdutoDTO adicionar(ProdutoDTO produtoDTO){
        //removendo o id para conseguir fazer o cadastro
        produtoDTO.setId(null);

        //criar um objeto de mapeamento.
        ModelMapper mapper = new ModelMapper();

        //converter o nosso produtoDTO em um produto
        Produto produto = mapper.map(produtoDTO, Produto.class);

        //salvar o produto no banco
        produto = produtoRepository.save(produto);

        produtoDTO.setId(produto.getId());

        //retornar o produtoDTO atualizado

        return produtoDTO;
    }

    /**
     * Metodo para deletar o produto por id.
     * @param id do produto a ser deletado.
     */
    public void deletar(Integer id){
        //verificar se o produto existe
        Optional<Produto> produto = produtoRepository.findById(id);

        if(produto.isEmpty()){
            throw new ResourceNotFoundException("Nao foi possivel deletar o produto com o id: " + id + " - Produto nao existe");
        }

        //deleta o produto pelo id
        produtoRepository.deleteById(id);;
    }

    /**
     * Metodo para atualizar o produto na lista
     * @param produto que sera atualizado.
     * @param id do produto.
     * @return Retorna o produto apos atualizar a lista.
     */
    public ProdutoDTO atualizar(Integer id,ProdutoDTO produtoDto){

        // passar o id para o produtoDto
        produtoDto.setId(id);

        //criar um objeto de mapeamento
        ModelMapper mapper = new ModelMapper();

        //converter o ProdutoDto em um produto.
        Produto produto = mapper.map(produtoDto, Produto.class);

        //atualizar o produto no banco  de dados.
        produtoRepository.save(produto);

        //retornar o produtoDto atualizado
        return produtoDto;
    }
}
