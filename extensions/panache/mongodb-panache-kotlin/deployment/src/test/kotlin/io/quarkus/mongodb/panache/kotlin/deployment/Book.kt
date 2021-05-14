package io.quarkus.mongodb.panache.kotlin.deployment

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import io.quarkus.mongodb.panache.PanacheUpdate
import io.quarkus.mongodb.panache.kotlin.PanacheMongoCompanion
import io.quarkus.mongodb.panache.kotlin.PanacheMongoEntity
import io.quarkus.mongodb.panache.kotlin.PanacheQuery
				import io.quarkus.mongodb.panache.kotlin.runtime.KotlinMongoOperations.Companion.INSTANCE
import io.quarkus.panache.common.Parameters
import io.quarkus.panache.common.Sort
import org.bson.Document
import org.bson.types.ObjectId
import java.util.stream.Stream

/**
 * This class is used by TestEnhancers to validate the bytecode generation.  Each method on PanacheMongoCompanion is
 * manually implemented to give us a compiler generated metric against which to validate the quarkus generated bytecode.
 * TestEnhancers further validates that all @GenerateBridge annotated methods are represented by a 'target_' method
 * here.
 */
@Suppress("UNCHECKED_CAST", "unused")
class Book : PanacheMongoEntity<Book>() {
    companion object : PanacheMongoCompanion<Book> {
        fun target_count(): Long
            = INSTANCE.count(Book::class.java)

        fun target_count(query: Document): Long
            = INSTANCE.count(Book::class.java, query)

        fun target_count(query: String, params: Map<String, Any?>): Long
            = INSTANCE.count(Book::class.java, query, params)

        fun target_count(query: String, params: Parameters): Long
            = INSTANCE.count(Book::class.java, query, params)

        fun target_count(query: String, vararg params: Any?): Long
            = INSTANCE.count(Book::class.java, query, *params)

        fun target_delete(query: Document): Long
            = INSTANCE.delete(Book::class.java, query)

        fun target_delete(query: String, params: Map<String, Any?>): Long
            = INSTANCE.delete(Book::class.java, query, params)

        fun target_delete(query: String, params: Parameters): Long
            = INSTANCE.delete(Book::class.java, query, params)

        fun target_delete(query: String, vararg params: Any?): Long
            = INSTANCE.delete(Book::class.java, query, *params)

        fun target_deleteAll(): Long
            = INSTANCE.deleteAll(Book::class.java)

        fun target_deleteById(id: ObjectId): Boolean
            = INSTANCE.deleteById(Book::class.java, id)

        fun target_find(query: Document): PanacheQuery<Book>
            = INSTANCE.find(Book::class.java, query) as PanacheQuery<Book>

        fun target_find(query: Document, sort: Document): PanacheQuery<Book>
            = INSTANCE.find(Book::class.java, query, sort) as PanacheQuery<Book>

        fun target_find(query: String, params: Map<String, Any?>): PanacheQuery<Book>
            = INSTANCE.find(Book::class.java, query, params) as PanacheQuery<Book>

        fun target_find(query: String, params: Parameters): PanacheQuery<Book>
            = INSTANCE.find(Book::class.java, query, params) as PanacheQuery<Book>

        fun target_find(query: String, sort: Sort, params: Map<String, Any?>): PanacheQuery<Book>
            = INSTANCE.find(Book::class.java, query, sort, params) as PanacheQuery<Book>

        fun target_find(query: String, sort: Sort, params: Parameters): PanacheQuery<Book>
            = INSTANCE.find(Book::class.java, query, sort, params) as PanacheQuery<Book>

        fun target_find(query: String, sort: Sort, vararg params: Any?): PanacheQuery<Book>
            = INSTANCE.find(Book::class.java, query, sort, *params) as PanacheQuery<Book>

        fun target_find(query: String, vararg params: Any?): PanacheQuery<Book>
            = INSTANCE.find(Book::class.java, query, *params) as PanacheQuery<Book>

        fun target_findAll(): PanacheQuery<Book>
            = INSTANCE.findAll(Book::class.java) as PanacheQuery<Book>

        fun target_findAll(sort: Sort): PanacheQuery<Book>
            = INSTANCE.findAll(Book::class.java, sort) as PanacheQuery<Book>

        fun target_findById(id: ObjectId): Book?
            = INSTANCE.findById(Book::class.java, id) as Book?

        fun target_list(query: Document): List<Book>
            = INSTANCE.list(Book::class.java, query) as List<Book>

        fun target_list(query: Document, sort: Document): List<Book>
            = INSTANCE.list(Book::class.java, query, sort) as List<Book>

        fun target_list(query: String, params: Map<String, Any?>): List<Book>
            = INSTANCE.list(Book::class.java, query, params) as List<Book>

        fun target_list(query: String, params: Parameters): List<Book>
            = INSTANCE.list(Book::class.java, query, params) as List<Book>

        fun target_list(query: String, sort: Sort, params: Map<String, Any?>): List<Book>
            = INSTANCE.list(Book::class.java, query, sort, params) as List<Book>

        fun target_list(query: String, sort: Sort, params: Parameters): List<Book>
            = INSTANCE.list(Book::class.java, query, sort, params) as List<Book>

        fun target_list(query: String, sort: Sort, vararg params: Any?): List<Book>
            = INSTANCE.list(Book::class.java, query, sort, *params) as List<Book>

        fun target_list(query: String, vararg params: Any?): List<Book>
            = INSTANCE.list(Book::class.java, query, *params) as List<Book>

        fun target_listAll(): List<Book>
            = INSTANCE.listAll(Book::class.java) as List<Book>

        fun target_listAll(sort: Sort): List<Book>
            = INSTANCE.listAll(Book::class.java, sort) as List<Book>

        fun target_mongoCollection(): MongoCollection<Book>
            = INSTANCE.mongoCollection(Book::class.java) as MongoCollection<Book>

        fun target_mongoDatabase(): MongoDatabase
            = INSTANCE.mongoDatabase(Book::class.java)

        fun target_stream(query: Document): Stream<Book>
            = INSTANCE.stream(Book::class.java, query) as Stream<Book>

        fun target_stream(query: Document, sort: Document): Stream<Book>
            = INSTANCE.stream(Book::class.java, query, sort) as Stream<Book>

        fun target_stream(query: String, params: Map<String, Any?>): Stream<Book>
            = INSTANCE.stream(Book::class.java, query, params) as Stream<Book>

        fun target_stream(query: String, params: Parameters): Stream<Book>
            = INSTANCE.stream(Book::class.java, query, params) as Stream<Book>

        fun target_stream(query: String, sort: Sort, params: Map<String, Any?>): Stream<Book>
            = INSTANCE.stream(Book::class.java, query, sort, params) as Stream<Book>

        fun target_stream(query: String, sort: Sort, params: Parameters): Stream<Book>
            = INSTANCE.stream(Book::class.java, query, sort, params) as Stream<Book>

        fun target_stream(query: String, sort: Sort, vararg params: Any?): Stream<Book>
            = INSTANCE.stream(Book::class.java, query, sort, *params) as Stream<Book>

        fun target_stream(query: String, vararg params: Any?): Stream<Book>
            = INSTANCE.stream(Book::class.java, query, *params) as Stream<Book>

        fun target_streamAll(): Stream<Book>
            = INSTANCE.streamAll(Book::class.java) as Stream<Book>

        fun target_streamAll(sort: Sort): Stream<Book>
            = INSTANCE.streamAll(Book::class.java, sort) as Stream<Book>

        fun target_update(update: String, params: Map<String, Any?>): PanacheUpdate
            = INSTANCE.update(Book::class.java, update, params)

        fun target_update(update: String, params: Parameters): PanacheUpdate
            = INSTANCE.update(Book::class.java, update, params)

        fun target_update(update: String, vararg params: Any?): PanacheUpdate
            = INSTANCE.update(Book::class.java, update, *params)

    }
}
