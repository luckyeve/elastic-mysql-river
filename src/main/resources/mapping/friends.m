curl -XPOST localhost:9200/lucky/_mapping/friends -d '
{
  "properties": {
    "uname": {
      "type": "long",
      "store": "yes",
      "index": "not_analyzed"
    },
    "Uname": {
      "type": "long",
      "store": "yes",
      "index": "not_analyzed"
    },
    "fnickname": {
      "type": "text",
      "store": "yes",
      "term_vector": "with_positions_offsets",
      "analyzer": "ik_max_word",
      "search_analyzer": "ik_max_word",
      "include_in_all": "true",
      "boost": 8
    },
    "funame": {
      "type": "long",
      "store": "yes",
      "index": "not_analyzed"
    },
    "type": {
      "type": "integer",
      "store": "yes",
      "index": "not_analyzed"
    }
  }
}'