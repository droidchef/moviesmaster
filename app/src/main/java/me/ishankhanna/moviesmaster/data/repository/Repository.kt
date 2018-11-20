package me.ishankhanna.moviesmaster.data.repository

interface Repository<T> {

    fun getAll() : Collection<T>

    fun addAll(items: Collection<T>)

    fun clear()

}