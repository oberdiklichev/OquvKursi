data class Student(val id: Int, val name: String)
data class Teacher(val id: Int, val name: String, val field: String)
data class Course(
    val id: Int,
    val name: String,
    val studentIds: MutableList<Int>,
    var teacherId: Int,
    val field: String
)

class Manager {
    private val students = mutableListOf<Student>()
    private val teachers = mutableListOf<Teacher>()
    private val courses = mutableListOf<Course>()
    private val fields = listOf("math", "english", "ai")
    private val regexName = Regex("^[A-Z][a-z]{2,}\$")

    fun addStudent(name: String): Boolean {
        if (name.matches(regexName)) {
            students.add(Student(students.size + 1, name))
            return true
        }
        return false
    }

    fun addTeacher(name: String, field: String): Boolean {
        if (name.matches(regexName) && field in fields) {
            teachers.add(Teacher(teachers.size + 1, name, field))
            return true
        }
        return false
    }

    fun addCourse(name: String, teacherId: Int, field: String): Boolean {
        val teacher = teachers.find { it.id == teacherId }
        if (teacher != null && teacher.field == field) {
            courses.add(Course(courses.size + 1, name, mutableListOf(), teacherId, field))
            return true
        }
        return false
    }

    fun getStudents() = students
    fun getTeachers() = teachers
    fun getCourses() = courses

    fun addStudentToCourse(courseId: Int, studentId: Int): Boolean {
        val course = courses.find { it.id == courseId }
        val student = students.find { it.id == studentId }
        if (course != null && student != null && studentId !in course.studentIds) {
            course.studentIds.add(studentId)
            return true
        }
        return false
    }

    fun removeStudentFromCourse(courseId: Int, studentId: Int): Boolean {
        val course = courses.find { it.id == courseId }
        if (course != null && studentId in course.studentIds) {
            course.studentIds.remove(studentId)
            return true
        }
        return false
    }

    fun changeCourseTeacher(courseId: Int, teacherId: Int): Boolean {
        val course = courses.find { it.id == courseId }
        val teacher = teachers.find { it.id == teacherId }
        if (course != null && teacher != null && course.field == teacher.field) {
            course.teacherId = teacherId
            return true
        }
        return false
    }
}

fun main() {
    val m = Manager()

    val yashil = "\u001B[32m"
    val qizil = "\u001B[31m"
    val sariq = "\u001B[33m"
    val reset = "\u001B[0m"

    while (true) {
        println(
            """
            ===============================
                     O'QUV TIZIMI MENYUSI
            ===============================
            1.  O'quvchi qo'shish
            2.  O'qituvchi qo'shish
            3.  Kurs qo'shish
            4.  O'quvchilar ro'yxatini ko'rish
            5.  O'qituvchilar ro'yxatini ko'rish
            6.  Kurslar ro'yxatini ko'rish
            7.  Kursga o'quvchi qo'shish
            8.  Kursdan o'quvchini olib tashlash
            9.  Kurs o'qituvchisini almashtirish
            0.  Chiqish
            ===============================
        """.trimIndent()
        )

        print("Tanlovni kiriting: ")
        when (readln().trim()) {
            "1" -> {
                print("O'quvchi ismini kiriting (Masalan: John): ")
                val name = readln().trim()
                if (m.addStudent(name))
                    println("${yashil}O'quvchi muvaffaqiyatli qo'shildi.$reset")
                else
                    println("${qizil}Noto'g'ri ism! Ism bosh harf bilan boshlanishi kerak.$reset")
            }

            "2" -> {
                print("O'qituvchi ismini kiriting (Masalan: Alice): ")
                val name = readln().trim()
                print("O'qituvchi yo'nalishini kiriting (math, english, ai): ")
                val field = readln().lowercase().trim()
                if (m.addTeacher(name, field))
                    println("${yashil}O'qituvchi muvaffaqiyatli qo'shildi.$reset")
                else
                    println("${qizil}Noto'g'ri ma'lumot! Ism yoki yo'nalishni tekshiring.$reset")
            }

            "3" -> {
                if (m.getTeachers().isEmpty()) {
                    println("${sariq}Avval kamida bitta o'qituvchi qo'shing.$reset")
                    continue
                }
                println("---- O'qituvchilar ro'yxati ----")
                m.getTeachers().forEach { println("${it.id}. ${it.name} (${it.field})") }

                print("Kurs nomini kiriting: ")
                val name = readln().trim()
                print("O'qituvchi ID raqamini kiriting: ")
                val teacherId = readln().toIntOrNull() ?: -1
                print("Kurs yo'nalishini kiriting (math, english, ai): ")
                val field = readln().lowercase().trim()

                if (m.addCourse(name, teacherId, field))
                    println("${yashil}Kurs muvaffaqiyatli qo'shildi.$reset")
                else
                    println("${qizil}Kurs qo'shilmadi! O'qituvchi yoki yo'nalish mos emas.$reset")
            }

            "4" -> {
                println("---- O'quvchilar ro'yxati ----")
                if (m.getStudents().isEmpty()) println("${sariq}Hozircha o'quvchi yo'q.$reset")
                else m.getStudents().forEach { println("${it.id}. ${it.name}") }
            }

            "5" -> {
                println("---- O'qituvchilar ro'yxati ----")
                if (m.getTeachers().isEmpty()) println("${sariq}Hozircha o'qituvchi yo'q.$reset")
                else m.getTeachers().forEach { println("${it.id}. ${it.name} - ${it.field}") }
            }

            "6" -> {
                println("---- Kurslar ro'yxati ----")
                if (m.getCourses().isEmpty()) println("${sariq}Hozircha kurs yo'q.$reset")
                else m.getCourses().forEach {
                    println(
                        "${it.id}. ${it.name} | O'qituvchi ID: ${it.teacherId} | Yo'nalish: ${it.field} | O'quvchilar: ${it.studentIds}"
                    )
                }
            }

            "7" -> {
                if (m.getCourses().isEmpty() || m.getStudents().isEmpty()) {
                    println("${sariq}Avval kurs va o'quvchi qo'shing.$reset")
                    continue
                }
                println("---- Kurslar ----")
                m.getCourses().forEach { println("${it.id}. ${it.name}") }
                print("Kurs ID: ")
                val courseId = readln().toIntOrNull() ?: -1

                println("---- O'quvchilar ----")
                m.getStudents().forEach { println("${it.id}. ${it.name}") }
                print("O'quvchi ID: ")
                val studentId = readln().toIntOrNull() ?: -1

                if (m.addStudentToCourse(courseId, studentId))
                    println("${yashil}O'quvchi kursga qo'shildi.$reset")
                else
                    println("${qizil}Qo'shib bo'lmadi! ID yoki kursni tekshiring.$reset")
            }

            "8" -> {
                if (m.getCourses().isEmpty()) {
                    println("${sariq}Avval kurs qo'shing.$reset")
                    continue
                }
                println("---- Kurslar ----")
                m.getCourses().forEach { println("${it.id}. ${it.name}") }
                print("Kurs ID: ")
                val courseId = readln().toIntOrNull() ?: -1

                val course = m.getCourses().find { it.id == courseId }
                if (course == null || course.studentIds.isEmpty()) {
                    println("${sariq}Bu kursda o'quvchilar yo'q.$reset")
                    continue
                }

                println("---- Kursdagi o'quvchilar ----")
                course.studentIds.forEach {
                    val s = m.getStudents().find { st -> st.id == it }
                    println("${s?.id}. ${s?.name}")
                }
                print("O'chiriladigan o'quvchi ID: ")
                val studentId = readln().toIntOrNull() ?: -1

                if (m.removeStudentFromCourse(courseId, studentId))
                    println("${yashil}O'quvchi kursdan olib tashlandi.$reset")
                else
                    println("${qizil}O'quvchini olib tashlab bo'lmadi.$reset")
            }

            "9" -> {
                if (m.getCourses().isEmpty() || m.getTeachers().isEmpty()) {
                    println("${sariq}Avval kurs va o'qituvchi qo'shing.$reset")
                    continue
                }
                println("---- Kurslar ----")
                m.getCourses().forEach { println("${it.id}. ${it.name} (${it.field})") }
                print("Kurs ID: ")
                val courseId = readln().toIntOrNull() ?: -1

                println("---- O'qituvchilar ----")
                m.getTeachers().forEach { println("${it.id}. ${it.name} - ${it.field}") }
                print("Yangi o'qituvchi ID: ")
                val teacherId = readln().toIntOrNull() ?: -1

                if (m.changeCourseTeacher(courseId, teacherId))
                    println("${yashil}Kurs o'qituvchisi o'zgartirildi.$reset")
                else
                    println("${qizil}O'qituvchi yo'nalishi mos emas yoki ID noto'g'ri.$reset")
            }

            "0" -> {
                println("${sariq}Dasturdan chiqildi.$reset")
                break
            }

            else -> println("${sariq}Noto'g'ri tanlov! Qaytadan urinib ko'ring.$reset")
        }

        println()
    }
}
