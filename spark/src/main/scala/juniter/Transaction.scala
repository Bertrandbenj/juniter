package juniter

case class Transaction( version: Int ,
                   currency: String,
                   locktime: Int,
                   thash: String,
                   blockstamp: String,
                   blockstampTime: String,
                   issuers: Array[String],
                   inputs: Array[String],
                   outputs: Array[String],
                   unlocks: Array[String],
                   signatures: Array[String],
                   comment: String)
