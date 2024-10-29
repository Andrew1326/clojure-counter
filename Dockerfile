# Use Debian-based Clojure image for broader compatibility
FROM clojure:tools-deps-bullseye

# Set working directory
WORKDIR /app

# Copy the deps.edn file if you need to define dependencies
COPY deps.edn .

# Copy the Clojure source code
COPY counter.clj .

# Expose the port the app runs on
EXPOSE 3000

# Run the application using the -M flag with the main namespace
CMD ["clojure", "-M", "-m", "counter"]
