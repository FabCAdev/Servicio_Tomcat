# Usamos una imagen oficial de Tomcat 10 con Java 17
FROM tomcat:10.1-jdk17

# Limpiamos las aplicaciones por defecto de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiamos tu archivo compilado y lo nombramos ROOT.war para que sea la app principal
# OJO: Cambia "dist/tu_proyecto.war" por la ruta y nombre real de tu archivo .war
COPY dist/API_HealthBlood.war /usr/local/tomcat/webapps/ROOT.war

# Exponemos el puerto de Tomcat
EXPOSE 8080

# Comando para iniciar el servidor
CMD ["catalina.sh", "run"]