package team.cesea.Interview.activities


class ListModel(id: Int, name: String, mail: String) {
    var id: Int = 0
        internal set
    var name: String
        internal set
    var mail: String
        internal set

    init {
        this.id = id
        this.name = name
        this.mail = mail

    }
}
