import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import CapabilityTableRow from "./CapabilityTableRow";
import TreeMenu from 'react-simple-tree-menu'
// import TreeViewMenu from 'react-simple-tree-menu'

export default class Capability extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            environments: [],
            environmentName: this.props.match.params.name,
            environmentId: '',
            capabilities: [],
            tree_root: {}
        };
    }

    async componentDidMount() {
        const environmentResponse = await fetch(`${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`);
        const environmentData = await environmentResponse.json();
        this.setState({environmentId: environmentData.environmentId});

        const capabilityResponse = await fetch(`${process.env.REACT_APP_API_URL}/capability/getallbyenvironment/${this.state.environmentId}`);
        const capabilityData = await capabilityResponse.json();

        // capabilityData.map(i=>i.children=null);

        capabilityData.forEach((cap) => {
            cap["key"] = cap.capabilityId
            cap["label"] = cap.capabilityName
        })

        this.setState({capabilities: capabilityData});
        capabilityData[0].parentCapabilityId = null;
        
        this.createDataTree(this.state.capabilities);
    }

    createDataTree(data) {
    let arr = []
    const idMapping = data.reduce((acc, cap, i) => {
    acc[cap.capabilityId] = i;
    return acc;
    }, {});
    let root;
    data.forEach(cap => {
    if (cap.parentCapabilityId === null) {
        root = cap;
        return;
    }
    const parentCap = data[idMapping[cap.parentCapabilityId]];
    parentCap.nodes = [...(parentCap.nodes || []), cap];
    })
    arr.push(root);
    this.setState({tree_root: arr})
    };

    capabilityTable() {
        return this.state.capabilities.map((row, i) => {
            return <CapabilityTableRow obj={ row } key={ i }/>
        })
    }

    render() {
        const environmentName = this.props.match.params.name;
        console.log(this.state.tree_root)
        return(
            <div className="jumbotron">
                <nav aria-label="breadcrumb">
                    <ol className="breadcrumb">
                        <li className="breadcrumb-item"><Link to={`/`}>Home</Link></li>
                        <li className="breadcrumb-item"><Link to={`/environment/${environmentName}`}>{environmentName}</Link></li>
                        <li className="breadcrumb-item">Capabilities</li>
                    </ol>
                </nav>
                <h1 className='display-4'>Capabilities</h1>
                <br/><br/>
                <TreeMenu
                    cacheSearch
                    data={this.state.tree_root}
                    debounceTime={125}
                    disableKeyboard={false}
                    hasSearch={false}
                    onClickItem={function noRefCheck(){}}
                    resetOpenNodesOnDataUpdate={false}
                    />
                <div className="row">
                    <div className="col-sm-6">
                    {/* <div><pre>{JSON.stringify(this.state.tree_root, null, 2) }</pre></div>; */}
                <table className='table table-striped'>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Parent Id</th>
                        <th>Level</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    { this.capabilityTable() }
                    </tbody>
                </table>
                    </div>
                    <div className="col-sm-6">
                    <div><pre>{JSON.stringify(this.state.tree_root, null, 2) }</pre></div>;

                    <div className="text-center">
                        <Link to={'edit'}>
                            <input type="button" value="Edit" className="btn btn-secondary input-button hoverable"/>
                        </Link>
                    </div>
                        <br/>
                    <div className="text-center">
                        <Link to={'childcapability'}>
                            <input type="button" value="Child Capability" className="btn btn-secondary input-button hoverable"/>
                        </Link>
                    </div>
                    </div>
                </div>
            </div>
        )
    }








}