# The github boilerplate I made to auto push to github.
function gb
{
  git add .
  git commit -a -m "$(Get-Date) automated commit"
  git push
}
