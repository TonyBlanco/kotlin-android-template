package rocka.template.auth

object MockAuthRepository {
    private const val SIGN_IN_DELAY_MS = 1_200L

    fun signIn(
        email: String,
        password: String,
        onComplete: (Result<Unit>) -> Unit,
    ) {
        Thread {
            Thread.sleep(SIGN_IN_DELAY_MS)
            onComplete(Result.success(Unit))
        }.start()
    }
}
