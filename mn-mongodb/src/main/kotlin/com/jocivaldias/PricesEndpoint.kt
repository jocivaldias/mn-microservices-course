package com.jocivaldias

import com.fasterxml.jackson.databind.node.ObjectNode
import com.mongodb.client.result.InsertOneResult
import com.mongodb.reactivestreams.client.MongoClient
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.reactivex.Flowable
import org.bson.Document
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller("/prices")
class PricesEndpoint(val mongoClient: MongoClient) {

    private val log: Logger = LoggerFactory.getLogger(PricesEndpoint::class.java)
    
    @Get("/")
    fun fetch(): Flowable<Document> {
        val mongoCollection = getCollection()

        return Flowable.fromPublisher(mongoCollection.find())//find all entries
    }
    
    @Post("/")
    fun insert(@Body json: ObjectNode): Flowable<InsertOneResult> {
        val mongoCollection = getCollection()
        val doc = Document.parse(json.toString())
        log.info("Inserted: {}", doc)
        return Flowable.fromPublisher(mongoCollection.insertOne(doc))
    }

    private fun getCollection() = mongoClient.getDatabase("prices").getCollection("example")

}