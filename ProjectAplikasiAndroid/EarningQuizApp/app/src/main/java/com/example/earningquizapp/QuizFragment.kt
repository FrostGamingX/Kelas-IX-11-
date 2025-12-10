class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private lateinit var questionList: List<Question>
    private var currentQuestion = 0
    private var score = 0L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil 10 pertanyaan acak dari database
        lifecycleScope.launch {
            questionList = AppDatabase.getDatabase(requireContext()).questionDao().getRandomQuestions(10)
            showQuestion()
        }

        binding.btnNext.setOnClickListener {
            checkAnswer()
        }
    }

    private fun showQuestion() {
        val q = questionList[currentQuestion]
        binding.tvQuestionNumber.text = "Pertanyaan ${currentQuestion + 1}/${questionList.size}"
        binding.tvQuestion.text = q.question
        binding.rb1.text = q.option1
        binding.rb2.text = q.option2
        binding.rb3.text = q.option3
        binding.rb4.text = q.option4
        binding.radioGroup.clearCheck()
    }

    private fun checkAnswer() {
        val selected = when (binding.radioGroup.checkedRadioButtonId) {
            R.id.rb1 -> 1
            R.id.rb2 -> 2
            R.id.rb3 -> 3
            R.id.rb4 -> 4
            else -> 0
        }

        if (selected == questionList[currentQuestion].correctOption) {
            score += questionList[currentQuestion].points
            Toast.makeText(requireContext(), "+${questionList[currentQuestion].points} poin!", Toast.LENGTH_SHORT).show()
        }

        currentQuestion++
        if (currentQuestion < questionList.size) {
            showQuestion()
        } else {
            // Kuis selesai → tambah saldo
            lifecycleScope.launch {
                val user = AppDatabase.getDatabase(requireContext()).userDao().getUser()
                val newBalance = user.balance + score * 100 // 1 poin = Rp 100 (contoh)
                AppDatabase.getDatabase(requireContext()).userDao().updateBalance(newBalance)
                findNavController().navigate(R.id.action_quiz_to_result, bundleOf("score" to score))
            }
        }
    }
}