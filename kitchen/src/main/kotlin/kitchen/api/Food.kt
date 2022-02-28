package kitchen.api

enum class Food(val supplierUrl: String) {
    PASTA("http://localhost:10000/api/smart/bake/pasta"),
    FISH("http://localhost:9000/api/smart/bake/fish"),
}
