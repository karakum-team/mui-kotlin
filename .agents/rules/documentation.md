# Documentation Portability and Privacy

These rules apply to all repository documentation, including README files, plans, history, verification
records, and agent instructions.

* **Never use absolute filesystem paths or home-directory paths** in prose, links, command examples, or
  pasted output. Use repository-relative or document-relative paths. For an external checkout or another
  machine-specific location, use a neutral placeholder such as `<fork-checkout>`.
* **Never include the user's personal information.** This includes names, email addresses, account or login
  names, personal profile or fork URLs, home-directory names, and device identifiers. Refer to people by
  neutral roles and to a personal fork as "the fork"; use the upstream project URL when a repository link
  is needed.
* Sanitize copied logs and command output before including them. Preserve the technical evidence without
  local paths or personal details.
* Before finalizing a documentation change, inspect all added and modified text, including link targets,
  for absolute paths and personal information. Remove any such details from the passages being edited.
