package com.draccoapp.poker.api.pagging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.draccoapp.poker.api.model.response.contract.Contract
import com.draccoapp.poker.api.service.ContractService
import com.draccoapp.poker.api.service.GlobalService
import java.io.IOException

class Paginacao(private val contractService: ContractService) : PagingSource<Int, Contract>() {
    override fun getRefreshKey(state: PagingState<Int, Contract>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }


    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Contract> {
        return try {
            val position = params.key ?: 1
            val response = contractService.listarContractsPaginados(position)

            return LoadResult.Page(
                data = response.data,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (position == response.totalPages) null else position + 1
            )
        } catch (e: IOException) {
            Log.i("ListaContracts", "load: ERRO NA PAGINAÇÃO  foi $e")
            LoadResult.Error(e)
        }
    }
}