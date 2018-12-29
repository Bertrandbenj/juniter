package juniter


case class Block( version: Short,
             nonce: Long,
             number: Int,
             powMin: Int,
             time: Long,
             medianTime: Long,
             membersCount: Int,
             monetaryMass: Long,
             unitbase: Int,
             issuersCount: Int,
             issuersFrame: Int,
             issuersFrameVar: Int,
             currency: String,
             issuer: String,
             signature: String,
             hash: String,
             parameters: String,
             previousHash: String,
             previousIssuer: String,
             inner_hash: String//,
             //dividend: Int
//             identities: Array[String],
//             joiners: Array[String],
//             actives: Array[String],
//             leavers: Array[String],
//             revoked: Array[String],
//             excluded: Array[String],
//             certifications: Array[String],
//             transactions: Array[Transaction]
                )