import boto3
import csv
from datetime import datetime, timezone

# Initialize the AWS EC2 client
ec2 = boto3.client("ec2")

# Retrieve all EBS snapshots
response = ec2.describe_snapshots(OwnerIds=["self"])

# Function to calculate the age of a snapshot in days
def calculate_snapshot_age(start_time):
    now = datetime.now(timezone.utc)
    age = now - start_time
    return age.days

# List to store snapshot details with their ages
snapshots_with_age = []

# Loop through all snapshots to get their ages
for snapshot in response["Snapshots"]:
    snapshot_id = snapshot["SnapshotId"]
    start_time = snapshot["StartTime"]
    age_in_days = calculate_snapshot_age(start_time)
    description = snapshot.get("Description", "No description")

    # Add snapshot details to the list
    snapshots_with_age.append({
        "Snapshot ID": snapshot_id,
        "Start Time": start_time.isoformat(),
        "Snapshot Age (days)": age_in_days,
        "Description": description
    })

# CSV file path where you want to save the data
csv_file_path = "snapshots_with_age.csv"

# Save the snapshot details to a CSV file
with open(csv_file_path, mode='w', newline='') as csvfile:
    fieldnames = ["Snapshot ID", "Start Time", "Snapshot Age (days)", "Description"]
    writer = csv.DictWriter(csvfile, fieldnames=fieldnames)

    # Write header
    writer.writeheader()

    # Write snapshot data
    for snapshot_info in snapshots_with_age:
        writer.writerow(snapshot_info)

print(f"Snapshot details saved to {csv_file_path}")