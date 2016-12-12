curl -XPOST localhost:9200/lucky/_mapping/users -d '
{
  "properties": {
    "nickname": {
      "type": "text",
      "store": "yes",
      "term_vector": "with_positions_offsets",
      "analyzer": "ik_max_word",
      "search_analyzer": "ik_max_word",
      "include_in_all": "true",
      "boost": 8
    },
    "photo": {
      "type": "keyword",
      "store": "yes",
      "index": "not_analyzed"
    },
    "Sex": {
      "type": "integer",
      "store": "yes",
      "index": "not_analyzed"
    },
    "zone": {
      "type": "integer",
      "store": "yes",
      "index": "not_analyzed"
    },
    "same_name_page_id": {
      "type": "long",
      "store": "yes",
      "index": "not_analyzed"
    },
    "same_name_page_name": {
      "type": "text",
      "store": "yes",
      "term_vector": "with_positions_offsets",
      "analyzer": "ik_max_word",
      "search_analyzer": "ik_max_word",
      "include_in_all": "true",
      "boost": 8
    },
    "v3_page_id": {
      "type": "long",
      "store": "yes",
      "index": "not_analyzed"
    },
    "state": {
      "type": "integer",
      "store": "yes",
      "index": "not_analyzed"
    }
  }
}'