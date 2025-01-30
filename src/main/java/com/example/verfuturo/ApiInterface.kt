import com.example.verfuturo.ApiRequest
import com.example.verfuturo.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {
    @POST("v1/chat/completions") // Asegúrate de que el endpoint sea el correcto
    fun obtenerPrediccion(
        @Header("Authorization") authHeader: String, // API Key
        @Body request: ApiRequest // Cuerpo de la solicitud
    ): Call<ApiResponse> // Debe coincidir con la respuesta esperada
}