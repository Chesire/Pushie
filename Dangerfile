def pr_matches_body?
  template = File.read(".github/pull_request_template.md")
  github.pr_body.gsub(/\s+/, "").eql? template.gsub(/\s+/, "")
end

# General
github.dismiss_out_of_range_messages

# PR title
failure "Please provide a title for the pull request" if github.pr_title.length < 5

# PR body
failure "Please provide a description for the pull request" if github.pr_body.length < 5
failure "Please fill in the pull request template" if pr_matches_body?

# PR other
warn "This PR does not have any assignees yet." unless github.pr_json["assignee"]
warn "PR is classed as Work in Progress" if github.pr_title.include? "[WIP]"
warn "Big PR" if git.lines_of_code > 500

# AndroidLint
lint_dir = "**/reports/lint-results*.xml"
Dir[lint_dir].each do |file_name|
  android_lint.skip_gradle_task = true
  android_lint.filtering = true
  android_lint.report_file = file_name
  android_lint.lint(inline_mode: true)
end

# CheckstyleFormat
checkstyle_format.base_path = Dir.pwd
checkstyle_format.report 'build/reports/detekt/detekt.xml'
