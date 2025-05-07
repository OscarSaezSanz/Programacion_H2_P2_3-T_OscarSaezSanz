import java.sql.*;
import java.util.*;

public class Cine {
    public static void main(String[] args) {
        // Datos de conexión a la base de datos MySQL
        String url = "jdbc:mysql://localhost:3306/cine";
        String usuario = "root";
        String contraseña = "curso";

        Scanner sc = new Scanner(System.in); // Scanner para leer entradas del usuario
        int opcion;
        String codigo;
        String nombre;
        int anio;
        int duracion;
        String genero;
        int directorId;

        try {
            // Establecer conexión con la base de datos
            Connection conexion = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println("¡Conexión exitosa!");

            do {
                // Mostrar menú
                System.out.println("\n-_-_-_-_-MENÚ-_-_-_-_-\n");
                System.out.println("1 - Ver películas");
                System.out.println("2 - Añadir película");
                System.out.println("3 - Eliminar película");
                System.out.println("4 - Modificar película");
                System.out.println("5 - Salir");
                System.out.print("Elige una opción: ");
                opcion = sc.nextInt();
                sc.nextLine(); // Limpiar el salto de línea pendiente

                switch (opcion) {
                    case 1:
                        // Ver películas registradas
                        System.out.println("\nListado de Películas:\n");

                        // Consulta SQL que une películas con sus directores
                        String consulta = """
                            SELECT p.codigo_pelicula, p.titulo, p.anio_estreno, p.duracion_min,
                                   p.genero, d.nombre AS director
                            FROM peliculas p
                            JOIN directores d ON p.director_id = d.id_director
                        """;

                        Statement stmt = conexion.createStatement();
                        ResultSet rs = stmt.executeQuery(consulta);

                        // Mostrar cada resultado
                        while (rs.next()) {
                            System.out.println("Código:     " + rs.getString("codigo_pelicula"));
                            System.out.println("Título:     " + rs.getString("titulo"));
                            System.out.println("Año:        " + rs.getInt("anio_estreno"));
                            System.out.println("Duración:   " + rs.getInt("duracion_min") + " min");
                            System.out.println("Género:     " + rs.getString("genero"));
                            System.out.println("Director:   " + rs.getString("director"));
                            System.out.println("\n──────────────────────────────────────\n");
                        }

                        rs.close();
                        stmt.close();
                        break;

                    case 2:
                        // Añadir una nueva película
                        System.out.println("\nAñadir película:\n");

                        System.out.print("Código de película: ");
                        codigo = sc.nextLine();

                        // Verificar si ya existe una película con ese código
                        String sqlCheck = "SELECT COUNT(*) FROM peliculas WHERE codigo_pelicula = ?";
                        PreparedStatement checkStmt = conexion.prepareStatement(sqlCheck);
                        checkStmt.setString(1, codigo);
                        ResultSet checkResult = checkStmt.executeQuery();
                        checkResult.next();
                        int existe = checkResult.getInt(1);
                        checkResult.close();
                        checkStmt.close();

                        if (existe > 0) {
                            System.out.println("Ya existe una película con ese código.");
                            break; // Salir del case si el código está duplicado
                        }

                        // Leer el resto de los datos de la película
                        System.out.print("Título de película: ");
                        nombre = sc.nextLine();

                        System.out.print("Año de la película: ");
                        anio = sc.nextInt();
                        sc.nextLine(); // Limpiar salto

                        System.out.print("Duración de la película (minutos): ");
                        duracion = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Género de la película: ");
                        genero = sc.nextLine();

                        System.out.print("ID del director: ");
                        directorId = sc.nextInt();
                        sc.nextLine();

                        // Consulta de inserción
                        String sqlInsert = """
                            INSERT INTO peliculas (codigo_pelicula, titulo, anio_estreno, duracion_min, genero, director_id)
                            VALUES (?, ?, ?, ?, ?, ?)
                        """;

                        PreparedStatement pstmt = conexion.prepareStatement(sqlInsert);
                        pstmt.setString(1, codigo);
                        pstmt.setString(2, nombre);
                        pstmt.setInt(3, anio);
                        pstmt.setInt(4, duracion);
                        pstmt.setString(5, genero);
                        pstmt.setInt(6, directorId);

                        // Ejecutar el INSERT
                        int filasAfectadas = pstmt.executeUpdate();

                        if (filasAfectadas > 0) {
                            System.out.println("Película añadida correctamente.");
                        } else {
                            System.out.println("Error al añadir la película.");
                        }

                        pstmt.close();
                        break;

                    case 3:
                        // Eliminar una película existente
                        System.out.println("\nEliminar película:\n");

                        System.out.print("Introduce el código de la película que quiere eliminar: ");
                        String codigoEliminar = sc.nextLine();

                        // Verificar si la película existe
                        String sqlBuscar = "SELECT COUNT(*) FROM peliculas WHERE codigo_pelicula = ?";
                        PreparedStatement buscarStmt = conexion.prepareStatement(sqlBuscar);
                        buscarStmt.setString(1, codigoEliminar);
                        ResultSet rsBuscar = buscarStmt.executeQuery();
                        rsBuscar.next();
                        int existePeli = rsBuscar.getInt(1);
                        rsBuscar.close();
                        buscarStmt.close();

                        if (existePeli == 0) {
                            System.out.println("No existe una película con ese código.");
                            break;
                        }

                        // Eliminar la película
                        String sqlEliminar = "DELETE FROM peliculas WHERE codigo_pelicula = ?";
                        PreparedStatement eliminarStmt = conexion.prepareStatement(sqlEliminar);
                        eliminarStmt.setString(1, codigoEliminar);

                        int filasEliminadas = eliminarStmt.executeUpdate();
                        if (filasEliminadas > 0) {
                            System.out.println("Película eliminada correctamente.");
                        } else {
                            System.out.println("No se ha podido eliminar la película.");
                        }

                        eliminarStmt.close();
                        break;

                    case 4:
                        // Modificar los datos de una película
                        System.out.println("\nModificar película:\n");

                        System.out.print("Introduce el código de la película que se quiere modificar: ");
                        String codigoModificar = sc.nextLine();

                        // Verificar si existe la película
                        String sqlBuscarMod = "SELECT COUNT(*) FROM peliculas WHERE codigo_pelicula = ?";
                        PreparedStatement buscarModStmt = conexion.prepareStatement(sqlBuscarMod);
                        buscarModStmt.setString(1, codigoModificar);
                        ResultSet rsMod = buscarModStmt.executeQuery();
                        rsMod.next();
                        int existeMod = rsMod.getInt(1);
                        rsMod.close();
                        buscarModStmt.close();

                        if (existeMod == 0) {
                            System.out.println("No existe una película con ese código.");
                            break;
                        }

                        // Leer los nuevos datos
                        System.out.print("Nuevo título: ");
                        nombre = sc.nextLine();

                        System.out.print("Nuevo año de estreno: ");
                        anio = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Nueva duración (minutos): ");
                        duracion = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Nuevo género: ");
                        genero = sc.nextLine();

                        System.out.print("Nuevo ID de director: ");
                        directorId = sc.nextInt();

                        // Consulta UPDATE para modificar la película
                        String sqlUpdate = """
                            UPDATE peliculas
                            SET titulo = ?, anio_estreno = ?, duracion_min = ?, genero = ?, director_id = ?
                            WHERE codigo_pelicula = ?
                        """;

                        PreparedStatement updateStmt = conexion.prepareStatement(sqlUpdate);
                        updateStmt.setString(1, nombre);
                        updateStmt.setInt(2, anio);
                        updateStmt.setInt(3, duracion);
                        updateStmt.setString(4, genero);
                        updateStmt.setInt(5, directorId);
                        updateStmt.setString(6, codigoModificar);

                        int filasActualizadas = updateStmt.executeUpdate();
                        if (filasActualizadas > 0) {
                            System.out.println("Película modificada correctamente.");
                        } else {
                            System.out.println("No se ha podido modificar la película.");
                        }

                        updateStmt.close();
                        break;

                    case 5:
                        // Salir del programa
                        System.out.println("\nSaliendo del programa...\n");
                        break;

                    default:
                        // Opción no reconocida
                        System.out.println("Opción no válida.");
                        break;
                }

            } while (opcion != 5); // Repetir el menú hasta que se elija salir

            // Cerrar recursos
            conexion.close();
            sc.close();
            System.out.println("Programa finalizado.");

        } catch (SQLException e) {
            // Captura errores relacionados con la base de datos
            System.out.println("Error de conexión o consulta: " + e.getMessage());
        }
    }
}

