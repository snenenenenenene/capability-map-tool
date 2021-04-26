import { Switch, Route } from 'react-router-dom';
import Home from './Home';
import NewEnvironment from './NewEnvironment';
import Environment from './Environment';

const Routes = (user) => {
    return (
      <main>
        <Switch>
          <Route exact path="/newenvironment" component={() => <NewEnvironment user={user} />}></Route>
          <Route exact path="/environment" component={() => <Environment user={user} />}></Route>
          <Route path="/*" component={()=> <Home user={user} />}></Route>
        </Switch>
      </main>
    )
}
export default Routes;