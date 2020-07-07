FROM openjdk:latest

RUN yum -y install cronie

# will have to change this to work inside the raspberry
COPY build/install/G2A /opt/G2A/
WORKDIR /opt/G2A/

# Copy g2a-cron file to the cron.d directory
COPY build/install/G2A/g2a-cron /etc/cron.d/g2a-cron

# Give execution rights on the cron job
RUN chmod 0644 /etc/cron.d/g2a-cron

# Apply cron job
RUN crontab /etc/cron.d/g2a-cron

# Create the log file to be able to run tail
RUN touch /var/log/g2a.log

# Run the command on container startup
CMD crond && tail -f /var/log/g2a.log
