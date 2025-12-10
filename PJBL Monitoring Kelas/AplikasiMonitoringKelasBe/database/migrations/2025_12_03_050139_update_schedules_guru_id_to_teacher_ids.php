<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;
use App\Models\Schedule;
use App\Models\Teacher;
use App\Models\User;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        // Update guru_id in schedules to point to teacher IDs instead of user IDs
        $schedules = Schedule::all();

        foreach ($schedules as $schedule) {
            // Find the teacher with matching email to the user who was the guru
            $user = User::find($schedule->guru_id);
            if ($user) {
                $teacher = Teacher::where('email', $user->email)->first();
                if ($teacher) {
                    $schedule->update(['guru_id' => $teacher->id]);
                }
            }
        }
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        // Revert guru_id back to user IDs
        $schedules = Schedule::all();

        foreach ($schedules as $schedule) {
            // Find the user with matching email to the teacher
            $teacher = Teacher::find($schedule->guru_id);
            if ($teacher) {
                $user = User::where('email', $teacher->email)->first();
                if ($user) {
                    $schedule->update(['guru_id' => $user->id]);
                }
            }
        }
    }
};
