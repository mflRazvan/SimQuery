import pkg_resources
import subprocess

# List all installed packages
installed_packages = [dist.project_name for dist in pkg_resources.working_set]

# Uninstall each package
for package in installed_packages:
    subprocess.call(['pip', 'uninstall', '-y', package])